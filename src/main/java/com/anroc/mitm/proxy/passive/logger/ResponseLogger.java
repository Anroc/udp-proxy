package com.anroc.mitm.proxy.passive.logger;

import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.passive.ResponseListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@Component
public class ResponseLogger implements ResponseListener {

    @Override
    public void listen(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        log.info("From server: {}", Arrays.toString(originalPacket.getData()));
    }
}
