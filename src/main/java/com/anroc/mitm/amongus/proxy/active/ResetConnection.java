package com.anroc.mitm.amongus.proxy.active;

import com.anroc.mitm.proxy.active.RequestInterceptor;
import com.anroc.mitm.proxy.data.UDPPackage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResetConnection implements RequestInterceptor {

    @Override
    public Optional<UDPPackage> intercept(Optional<UDPPackage> modifiedDataGramPacket, UDPPackage originalDatagramPacket) {
        if(modifiedDataGramPacket.isPresent()) {
            UDPPackage udpPackage = modifiedDataGramPacket.get();
            byte[] data = udpPackage.getData();
            if (data.length == 1 && data[0] == 9) {
                udpPackage.resetConnection();
            }
            return Optional.of(udpPackage);
        } else {
            return modifiedDataGramPacket;
        }
    }
}
