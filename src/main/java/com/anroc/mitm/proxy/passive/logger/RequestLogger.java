package com.anroc.mitm.proxy.passive.logger;

import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.passive.RequestListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@Component
public class RequestLogger implements RequestListener {

    @Override
    public void listen(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        log.info("From client: {}", Arrays.toString(originalPacket.getData()));
    }
}
