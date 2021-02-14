package com.anroc.mitm.amongus.proxy.passive;

import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.passive.ResponseListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResponseLogger implements ResponseListener {

    private final Logger logger;

    @Override
    public void listenResponse(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        logger.log("Server", modifiedPacket, originalPacket);
    }
}
