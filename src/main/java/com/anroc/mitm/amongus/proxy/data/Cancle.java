package com.anroc.mitm.amongus.proxy.data;

import com.anroc.mitm.proxy.data.UDPPackage;

public class Cancle extends AmongUsData {
    public Cancle(UDPPackage udpPackage) {
        super(udpPackage, Operation.CANCLE);
    }
}
