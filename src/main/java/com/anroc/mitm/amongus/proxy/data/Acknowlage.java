package com.anroc.mitm.amongus.proxy.data;

import com.anroc.mitm.proxy.data.UDPPackage;

public class Acknowlage extends AmongUsData {

    private final byte sequenceNumber;

    public Acknowlage(UDPPackage udpPackage) {
        super(udpPackage, Operation.ACK);
        this.sequenceNumber = udpPackage.getData()[2];
    }

    @Override
    public String toString() {
        return "Acknowlage{" +
                "sequenceNumber=" + sequenceNumber +
                '}';
    }
}
