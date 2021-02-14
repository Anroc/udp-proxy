package com.anroc.mitm.proxy.bindings.out;

import com.anroc.mitm.proxy.bindings.out.events.ServerPackageReceivedEvent;
import com.anroc.mitm.proxy.Proxy;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.ClientPackageProcessedEvent;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PreDestroy;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.Executor;

@Log4j2
public class OutBound {

    private final Proxy proxy;
    private DatagramSocket socket;
    private final OutBoundConfig config;
    private final Executor executor;

    @SneakyThrows
    public OutBound(OutBoundConfig config, Executor executor, Proxy proxy) {
        this.config = config;
        this.proxy = proxy;
        this.executor = executor;
        this.socket = new DatagramSocket();
    }

    @SneakyThrows
    public void send(ClientPackageProcessedEvent event) {
        DatagramPacket packet = event.getSource().toDatagramPacket(config.getInetAddress(), config.getPort());

        log.debug("[OUT] Sending packet to {}:{}", packet.getAddress().getCanonicalHostName(), packet.getPort());
        log.debug("[OUT] Content: {}", Arrays.toString(packet.getData()));

        socket.send(packet);
        executor.execute(() -> receive(event.getSource()));
    }

    @SneakyThrows
    public void receive(UDPPackage source) {
        log.debug("Waiting for server packages on {}:{}", socket.getLocalAddress().getHostAddress(), socket.getLocalPort());
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(50);
        try {
            socket.receive(packet);
            log.debug("Received packet from {}:{}", packet.getAddress().getCanonicalHostName(), packet.getPort());
            proxy.proxy(new ServerPackageReceivedEvent(UDPPackage.fromDatagramPacket(packet, source.getInetAddress(), source.getPort())));
        } catch (SocketTimeoutException e) {
            log.debug("No package received from Server.");
        } finally {
            if (source.shouldResetConnection()) {
                log.info("Resetting connection.");
                socket.close();
                this.socket = new DatagramSocket();
            }
        }
    }

    @PreDestroy
    public void close() {
        socket.close();
    }
}
