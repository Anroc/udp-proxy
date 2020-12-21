package com.anroc.mitm.proxy.events;

import com.anroc.mitm.proxy.data.UDPPackage;

public class ServerPackageProcessedEvent extends PackageProcessedEvent{
    public ServerPackageProcessedEvent(UDPPackage source) {
        super(source);
    }
}
