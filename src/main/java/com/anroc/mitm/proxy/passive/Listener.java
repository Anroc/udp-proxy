package com.anroc.mitm.proxy.passive;

import java.net.DatagramPacket;
import java.util.Optional;

public interface Listener {

    void listen(Optional<DatagramPacket> modifiedDataGramPacket, DatagramPacket originalDatagramPacket);
}
