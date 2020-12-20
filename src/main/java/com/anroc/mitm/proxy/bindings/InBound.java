package com.anroc.mitm.proxy.bindings;

import com.anroc.mitm.proxy.config.BindingConfig;
import com.anroc.mitm.proxy.Proxy;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Optional;


@Controller
public class InBound extends Thread {

    private final Proxy proxy;
    private final DatagramSocket socket;

    private boolean running;
    private byte[] buf = new byte[256];

    @Autowired
    @SneakyThrows
    public InBound(BindingConfig config, Proxy proxy) {
        this.socket = new DatagramSocket(config.getPort(), config.getInetAddress());
        this.proxy = proxy;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void listen() throws IOException {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            Optional<DatagramPacket> response = proxy.proxy(packet);

            if(response.isPresent()) {
                socket.send(packet);
            }
        }
    }

    @PreDestroy
    public void close() {
        socket.close();
    }
}
