package com.anroc.mitm.proxy;

import com.anroc.mitm.bindings.in.events.ClientPackageReceivedEvent;
import com.anroc.mitm.bindings.out.events.ServerPackageReceivedEvent;
import com.anroc.mitm.proxy.active.Interceptor;
import com.anroc.mitm.proxy.active.RequestInterceptor;
import com.anroc.mitm.proxy.active.ResponseInterceptor;
import com.anroc.mitm.bindings.out.OutBound;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.ClientPackageProcessedEvent;
import com.anroc.mitm.proxy.events.ServerPackageProcessedEvent;
import com.anroc.mitm.proxy.passive.Listener;
import com.anroc.mitm.proxy.passive.RequestListener;
import com.anroc.mitm.proxy.passive.ResponseListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.DatagramPacket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

@Data
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Proxy {

    private final OutBound outBound;
    private final ApplicationEventPublisher publisher;
    private final Executor executor;

    private final List<RequestInterceptor> requestInterceptors;
    private final List<ResponseInterceptor> responseInterceptors;

    private final List<RequestListener> requestListeners;
    private final List<ResponseListener> responseListeners;

    @PostConstruct
    public void init() {
        log.info("Found {} request listeners...", requestListeners.size());
        log.info("Found {} response listeners...", responseListeners.size());
        log.info("Found {} request interceptors...", requestInterceptors.size());
        log.info("Found {} response interceptors...", responseInterceptors.size());
    }

    @Async
    @EventListener
    public void proxy(ClientPackageReceivedEvent event) {
        UDPPackage source = event.getSource();
        UDPPackage originalPacketCopy = source.clone();

        Optional<UDPPackage> udpPackage = proxyActive(source, originalPacketCopy, getRequestInterceptors());
        executor.execute(() -> proxyPassive(udpPackage, originalPacketCopy, getRequestListeners()));

        if(! udpPackage.isPresent()) {
            return;
        }

        publisher.publishEvent(new ClientPackageProcessedEvent(udpPackage.get()));
    }

    @Async
    @EventListener(ServerPackageReceivedEvent.class)
    public void proxy(ServerPackageReceivedEvent event) {
        UDPPackage source = event.getSource();
        UDPPackage originalPacketCopy = source.clone();

        Optional<UDPPackage> udpPackage = proxyActive(source, originalPacketCopy, getResponseInterceptors());
        executor.execute(() -> proxyPassive(udpPackage, originalPacketCopy, getResponseListeners()));

        if(! udpPackage.isPresent()) {
            return;
        }
        publisher.publishEvent(new ServerPackageProcessedEvent(udpPackage.get()));
    }

    private Optional<UDPPackage> proxyActive(UDPPackage originalPacket, UDPPackage originalPacketCopy, List<? extends Interceptor> interceptors) {
        Optional<UDPPackage> modifiedPackage = Optional.of(originalPacket);
        for (Interceptor interceptor : interceptors) {
            modifiedPackage = interceptor.intercept(modifiedPackage, originalPacketCopy);
        }

        return modifiedPackage;
    }

    private void proxyPassive(Optional<UDPPackage> modifiedPackage, UDPPackage originalPacket, List<? extends Listener> listeners) {

        for (Listener listener : listeners) {
            listener.listen(modifiedPackage, originalPacket);
        }
    }
}
