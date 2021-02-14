package com.anroc.mitm.proxy.bindings.in;

import com.anroc.mitm.proxy.bindings.in.events.ClientPackageReceivedEvent;
import com.anroc.mitm.proxy.Proxy;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.ServerPackageProcessedEvent;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;


@Log4j2
public class InBound implements Runnable {

    private DatagramSocket socket;
    private final Proxy proxy;
    private boolean running;


    @SneakyThrows
    public InBound(InBoundConfig config, Proxy proxy) {
        log.info("Binding to {} addr {}", config.getPort(), config.getInetAddress());
        this.socket = new DatagramSocket(config.getPort(), config.getInetAddress());
        this.proxy = proxy;
    }

    public void receive() throws IOException {
        running = true;
        log.debug("Listening on {}:{}", socket.getLocalAddress().getCanonicalHostName(), socket.getPort());
        while (running) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            socket.receive(packet);
            log.debug("Received: {}", Arrays.toString(packet.getData()));

            log.debug("Received packet from: {}", packet.getAddress().getCanonicalHostName());
            proxy.proxy(new ClientPackageReceivedEvent(UDPPackage.fromDatagramPacket(packet)));
        }
    }

    @SneakyThrows
    public void send(ServerPackageProcessedEvent event) {
        UDPPackage source = event.getSource();
        DatagramPacket datagramPacket = source.toDatagramPacket();
        log.debug("Sending packet: {}", datagramPacket.getData());
        log.debug("Sending to {}:{}", datagramPacket.getSocketAddress(), datagramPacket.getPort());
        socket.send(datagramPacket);
    }

    @PreDestroy
    public void close() {
        socket.close();
    }

    @Override
    @SneakyThrows
    public void run() {
        receive();
    }
}
