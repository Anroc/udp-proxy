package com.anroc.mitm.proxy.passive;

import com.anroc.mitm.proxy.data.UDPPackage;

import java.util.Optional;

public interface RequestListener extends Listener {

    void listenRequest(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket);

    @Override
    default void listen(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        listenRequest(modifiedPacket, originalPacket);
    }
}
