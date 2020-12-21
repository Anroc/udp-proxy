package com.anroc.mitm.proxy.events;

import com.anroc.mitm.proxy.data.UDPPackage;

public class ClientPackageProcessedEvent extends PackageProcessedEvent {
    public ClientPackageProcessedEvent(UDPPackage source) {
        super(source);
    }
}
