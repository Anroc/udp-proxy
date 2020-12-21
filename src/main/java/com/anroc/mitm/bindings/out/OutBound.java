package com.anroc.mitm.bindings.out;

import com.anroc.mitm.bindings.out.events.ServerPackageReceivedEvent;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.ClientPackageProcessedEvent;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

@Log4j2
@Controller
public class OutBound {

    private final ApplicationEventPublisher publisher;
    private DatagramSocket socket;
    private OutBoundConfig config;

    private boolean running = true;

    @Autowired
    @SneakyThrows
    public OutBound(OutBoundConfig config, ApplicationEventPublisher publisher) {
        this.socket = new DatagramSocket();
        this.config = config;
        this.publisher = publisher;
    }

    @PostConstruct
    public void init() {
        log.info("Poxying to {}:{}", config.getAddress(), config.getPort());
    }

    @SneakyThrows
    @EventListener(ClientPackageProcessedEvent.class)
    public void send(ClientPackageProcessedEvent event) {
        DatagramPacket packet = event.getSource().toDatagramPacket(config.getInetAddress(), config.getPort());

        log.info("[OUT] Sending packet to {}:{}", packet.getAddress().getCanonicalHostName(), packet.getPort());
        socket.send(packet);
    }

    @Async
    @SneakyThrows
    @EventListener(ApplicationStartedEvent.class)
    public void receive() {
        while (running) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.debug("[OUT] Received packet from {}:{}", packet.getAddress().getCanonicalHostName(), packet.getPort());
            publisher.publishEvent(new ServerPackageReceivedEvent(UDPPackage.fromDatagramPacket(packet)));
        }
    }

    @PreDestroy
    public void close() {
        running = false;
        socket.close();
    }
}
