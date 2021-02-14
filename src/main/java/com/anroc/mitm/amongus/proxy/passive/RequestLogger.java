package com.anroc.mitm.amongus.proxy.passive;

import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.passive.RequestListener;
import com.anroc.mitm.proxy.passive.ResponseListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class RequestLogger implements RequestListener {

    private final Logger logger;

    @Override
    public void listenRequest(Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        logger.log("Client", modifiedPacket, originalPacket);
    }
}
