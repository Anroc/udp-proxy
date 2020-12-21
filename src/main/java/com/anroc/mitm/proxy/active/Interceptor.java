package com.anroc.mitm.proxy.active;

import com.anroc.mitm.proxy.data.UDPPackage;

import java.net.DatagramPacket;
import java.util.Optional;

public interface Interceptor {

    Optional<UDPPackage> intercept(Optional<UDPPackage> modifiedDataGramPacket, UDPPackage originalDatagramPacket);
}
