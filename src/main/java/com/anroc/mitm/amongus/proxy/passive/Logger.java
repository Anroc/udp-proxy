package com.anroc.mitm.amongus.proxy.passive;

import com.anroc.mitm.amongus.proxy.data.AmongUsData;
import com.anroc.mitm.amongus.proxy.data.Operation;
import com.anroc.mitm.proxy.data.UDPPackage;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

import static com.anroc.mitm.amongus.proxy.data.Operation.ACK;
import static com.anroc.mitm.amongus.proxy.data.Operation.PING;

@Log4j2
@Component
public class Logger {

    private final Operation[] IGNORED_OPERATIONS = new Operation[]{ PING, ACK };

    public void log(String from, Optional<UDPPackage> modifiedPacket, UDPPackage originalPacket) {
        if (! modifiedPacket.isPresent()) {
            log.info("{}: Dropped package: {}", from, originalPacket.toString());
            return;
        }

        UDPPackage udpPackage = modifiedPacket.get();
        if (udpPackage instanceof AmongUsData) {
            AmongUsData amongUsData = (AmongUsData) udpPackage;
            if (Arrays.stream(IGNORED_OPERATIONS).anyMatch(opt -> opt == amongUsData.getOperation())) {
                return;
            }
        }
        log.info("{}: {}", from, udpPackage.toString());
    }
}
