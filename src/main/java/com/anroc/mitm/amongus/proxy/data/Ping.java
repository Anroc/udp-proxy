package com.anroc.mitm.amongus.proxy.data;

import com.anroc.mitm.proxy.data.UDPPackage;

public class Ping extends AmongUsData {

    private final byte nonce;

    public Ping(UDPPackage udpPackage) {
        super(udpPackage, Operation.PING);
        this.nonce = udpPackage.getData()[2];
    }

    @Override
    public String toString() {
        return "Ping{" +
                "nonce=" + nonce +
                "} ";
    }
}
