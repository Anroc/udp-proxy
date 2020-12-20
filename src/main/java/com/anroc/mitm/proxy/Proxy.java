package com.anroc.mitm.proxy;

import com.anroc.mitm.proxy.active.Interceptor;
import com.anroc.mitm.proxy.active.RequestInterceptor;
import com.anroc.mitm.proxy.active.ResponseInterceptor;
import com.anroc.mitm.proxy.bindings.OutBound;
import com.anroc.mitm.proxy.passive.Listener;
import com.anroc.mitm.proxy.passive.RequestListener;
import com.anroc.mitm.proxy.passive.ResponseListener;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.net.DatagramPacket;
import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class Proxy {

    private final OutBound outBound;

    private final List<RequestInterceptor> requestInterceptors;
    private final List<ResponseInterceptor> responseInterceptors;

    private final List<RequestListener> requestListeners;
    private final List<ResponseListener> responseListeners;

    public Optional<DatagramPacket> proxy(DatagramPacket toServer) {
        DatagramPacket originalPacketCopy = copy(toServer);

        Optional<DatagramPacket> datagramPacket = proxyActive(toServer, originalPacketCopy, getRequestInterceptors());
        proxyPassive(datagramPacket, originalPacketCopy, getRequestListeners());

        if(! datagramPacket.isPresent()) {
            return Optional.empty();
        }
        DatagramPacket fromServer = this.outBound.send(toServer);

        originalPacketCopy = copy(fromServer);
        datagramPacket = proxyActive(fromServer, originalPacketCopy, getResponseInterceptors());
        proxyPassive(datagramPacket, originalPacketCopy, getResponseListeners());

        return datagramPacket;
    }

    private Optional<DatagramPacket> proxyActive(DatagramPacket originalPacket, DatagramPacket originalPacketCopy, List<? extends Interceptor> interceptors) {
        Optional<DatagramPacket> modifiedPackage = Optional.of(originalPacket);
        for (Interceptor interceptor : interceptors) {
            modifiedPackage = interceptor.intercept(modifiedPackage, originalPacketCopy);
        }

        return modifiedPackage;
    }

    private void proxyPassive(Optional<DatagramPacket> modifiedPackage, DatagramPacket originalPacket, List<? extends Listener> listeners) {
        for (Listener listener : listeners) {
            listener.listen(modifiedPackage, originalPacket);
        }
    }

    private DatagramPacket copy(@NonNull DatagramPacket toServer) {
        DatagramPacket originalPacketCopy = new DatagramPacket(
                toServer.getData(),
                toServer.getLength(),
                toServer.getAddress(),
                toServer.getPort()
        );
        return originalPacketCopy;
    }
}
