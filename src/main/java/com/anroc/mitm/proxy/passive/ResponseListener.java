package com.anroc.mitm.proxy.passive;

import com.anroc.mitm.proxy.data.UDPPackage;

import java.util.Optional;

public interface ResponseListener extends Listener {

    void listenResponse(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket);

    @Override
    default void listen(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        listenResponse(modifiedPacket, originalPacket);
    }
}
