package com.anroc.mitm.bindings.in;

import com.anroc.mitm.bindings.in.events.ClientPackageReceivedEvent;
import com.anroc.mitm.proxy.Proxy;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.ServerPackageProcessedEvent;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Optional;


@Log4j2
@Controller
public class InBound {

    private final DatagramSocket socket;
    private final ApplicationEventPublisher publisher;

    private boolean running;

    private InetAddress clientInetAddress;
    private int clientPort;

    @Autowired
    @SneakyThrows
    public InBound(InBoundConfig config, ApplicationEventPublisher publisher) {
        this.socket = new DatagramSocket(config.getPort(), config.getInetAddress());
        this.publisher = publisher;
    }

    @Async
    @EventListener(ApplicationStartedEvent.class)
    public void receive() throws IOException {
        running = true;
        log.debug("[IN] Listening on {}:{}", socket.getLocalAddress().getCanonicalHostName(), socket.getPort());
        while (running) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (clientInetAddress == null) {
                clientInetAddress = packet.getAddress();
            }
            if (clientPort == 0) {
                clientPort = packet.getPort();
            }
            log.debug("[IN] Received packet from: {}", packet.getAddress().getCanonicalHostName());
            publisher.publishEvent(new ClientPackageReceivedEvent(UDPPackage.fromDatagramPacket(packet)));
        }
    }

    @EventListener(ServerPackageProcessedEvent.class)
    public void send(ServerPackageProcessedEvent event) throws IOException {
        UDPPackage source = event.getSource();
        DatagramPacket datagramPacket = source.toDatagramPacket(clientInetAddress, clientPort);
        socket.send(datagramPacket);
    }

    @PreDestroy
    public void close() {
        socket.close();
    }
}
