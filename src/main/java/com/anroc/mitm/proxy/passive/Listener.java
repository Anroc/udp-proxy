package com.anroc.mitm.proxy.passive;

import com.anroc.mitm.proxy.data.UDPPackage;

import java.net.DatagramPacket;
import java.util.Optional;

public interface Listener {

    void listen(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket);
}
