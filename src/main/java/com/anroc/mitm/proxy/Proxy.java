package com.anroc.mitm.proxy;

import com.anroc.mitm.proxy.bindings.in.InBound;
import com.anroc.mitm.proxy.bindings.in.InBoundConfig;
import com.anroc.mitm.proxy.bindings.in.events.ClientPackageReceivedEvent;
import com.anroc.mitm.proxy.bindings.out.OutBound;
import com.anroc.mitm.proxy.bindings.out.OutBoundConfig;
import com.anroc.mitm.proxy.bindings.out.events.ServerPackageReceivedEvent;
import com.anroc.mitm.proxy.active.Interceptor;
import com.anroc.mitm.proxy.active.RequestInterceptor;
import com.anroc.mitm.proxy.active.ResponseInterceptor;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.ClientPackageProcessedEvent;
import com.anroc.mitm.proxy.events.ServerPackageProcessedEvent;
import com.anroc.mitm.proxy.mappers.RequestInterpreter;
import com.anroc.mitm.proxy.mappers.ResponseInterpreter;
import com.anroc.mitm.proxy.passive.Listener;
import com.anroc.mitm.proxy.passive.RequestListener;
import com.anroc.mitm.proxy.passive.ResponseListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

@Data
@Service
@Slf4j
public class Proxy {

    private final OutBound outBound;
    private final InBound inBound;
    private final Executor executor;

    private final List<RequestInterceptor> requestInterceptors;
    private final List<ResponseInterceptor> responseInterceptors;

    private final List<RequestListener> requestListeners;
    private final List<ResponseListener> responseListeners;

    @Autowired(required = false)
    private RequestInterpreter requestInterpreter;
    @Autowired(required = false)
    private ResponseInterpreter responseInterpreter;

    @Autowired
    public Proxy(
            Executor executor,
            List<RequestInterceptor> requestInterceptors,
            List<ResponseInterceptor> responseInterceptors,
            List<RequestListener> requestListeners,
            List<ResponseListener> responseListeners,
            InBoundConfig inBoundConfig,
            OutBoundConfig outBoundConfig) {
        this.executor = executor;
        this.requestInterceptors = requestInterceptors;
        this.responseInterceptors = responseInterceptors;
        this.requestListeners = requestListeners;
        this.responseListeners = responseListeners;
        this.outBound = new OutBound(outBoundConfig, executor, this);
        this.inBound = new InBound(inBoundConfig, this);
    }

    @PostConstruct
    public void init() {
        log.info("Found {} request listeners...", requestListeners.size());
        log.info("Found {} response listeners...", responseListeners.size());
        log.info("Found {} request interceptors...", requestInterceptors.size());
        log.info("Found {} response interceptors...", responseInterceptors.size());

        if (requestInterpreter == null) {
            requestInterpreter = (udpPackage -> udpPackage);
            log.info("No request interpreter registered.");
        } else {
            log.info("Found request interpreter.");
        }

        if (responseInterpreter == null) {
            responseInterpreter = (udpPackage -> udpPackage);
            log.info("No response interpreter registered.");
        } else {
            log.info("Found response interpreter.");
        }


        log.info("Starting client listener...");
        executor.execute(this.inBound);
    }

    public void proxy(ClientPackageReceivedEvent event) {
        UDPPackage source = requestInterpreter.interpret(event.getSource());
        UDPPackage originalPacketCopy = requestInterpreter.interpret(source.clone());

        Optional<UDPPackage> udpPackage = proxyActive(source, originalPacketCopy, getRequestInterceptors());
        executor.execute(() -> proxyPassive(udpPackage, originalPacketCopy, getRequestListeners()));

        if (!udpPackage.isPresent()) {
            return;
        }

        outBound.send(new ClientPackageProcessedEvent(udpPackage.get()));
    }

    public void proxy(ServerPackageReceivedEvent event) {
        UDPPackage source = responseInterpreter.interpret(event.getSource());
        UDPPackage originalPacketCopy = responseInterpreter.interpret(source.clone());

        Optional<UDPPackage> udpPackage = proxyActive(source, originalPacketCopy, getResponseInterceptors());
        executor.execute(() -> proxyPassive(udpPackage, originalPacketCopy, getResponseListeners()));

        if (!udpPackage.isPresent()) {
            return;
        }
        inBound.send(new ServerPackageProcessedEvent(udpPackage.get()));
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

    @PreDestroy
    public void onDestroy() {
        this.inBound.close();
        this.outBound.close();
    }
}
