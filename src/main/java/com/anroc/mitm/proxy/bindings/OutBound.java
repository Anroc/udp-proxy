package com.anroc.mitm.proxy.bindings;

import com.anroc.mitm.proxy.Proxy;
import com.anroc.mitm.proxy.config.BindingConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Controller
public class OutBound {

    private DatagramSocket socket;

    private byte[] buf = new byte[256];

    @Autowired
    @SneakyThrows
    public OutBound(BindingConfig config)  {
        socket = new DatagramSocket(config.getPort(), config.getInetAddress());
    }

    @SneakyThrows
    public DatagramPacket send(DatagramPacket packet) {
        socket.send(packet);

        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return packet;
    }

    @PreDestroy
    public void close() {
        socket.close();
    }
}
