package com.anroc.mitm.proxy.active;

import java.net.DatagramPacket;
import java.util.Optional;

public interface Interceptor {

    Optional<DatagramPacket> intercept(Optional<DatagramPacket> modifiedDataGramPacket, DatagramPacket originalDatagramPacket);
}
