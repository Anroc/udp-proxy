package com.anroc.mitm.amongus.proxy.mapper;

import com.anroc.mitm.amongus.proxy.data.*;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.mappers.RequestInterpreter;
import com.anroc.mitm.proxy.mappers.ResponseInterpreter;
import org.springframework.stereotype.Component;

@Component
public class AmongUsDataFactory implements RequestInterpreter<UDPPackage>, ResponseInterpreter<UDPPackage> {

    @Override
    public UDPPackage interpret(UDPPackage udpPackage) {
        byte[] data = udpPackage.getData();
        if (data.length == 0) {
            return udpPackage;
        }

        byte operation = udpPackage.getData()[0];

        switch (operation) {
            case 9:
                return new Cancle(udpPackage);
            case 10:
                return new Acknowlage(udpPackage);
            case 12:
                return new Ping(udpPackage);
            default:
                return udpPackage;
        }
    }
}
