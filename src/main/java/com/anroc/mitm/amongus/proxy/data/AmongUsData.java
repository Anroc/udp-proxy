package com.anroc.mitm.amongus.proxy.data;

import com.anroc.mitm.proxy.data.UDPPackage;
import lombok.Data;
import lombok.Getter;

public class AmongUsData extends UDPPackage {

    @Getter
    private final Operation operation;

    public AmongUsData(UDPPackage udpPackage, Operation operation) {
        super(udpPackage.getData(), udpPackage.getLength(), udpPackage.getInetAddress(), udpPackage.getPort());
        this.operation = operation;
    }


    @Override
    public String toString() {
        return String.format("%s\t%s", this.operation, super.toString());
    }
}
