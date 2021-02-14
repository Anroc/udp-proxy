package com.anroc.mitm.proxy.mappers;

import com.anroc.mitm.proxy.data.UDPPackage;

public interface ResponseInterpreter<T extends UDPPackage> {

    T interpret(UDPPackage udpPackage);
}
