package com.anroc.mitm.proxy.bindings.out.events;

import com.anroc.mitm.proxy.bindings.PackageReceivedEvent;
import com.anroc.mitm.proxy.data.UDPPackage;

public class ServerPackageReceivedEvent extends PackageReceivedEvent {
    public ServerPackageReceivedEvent(UDPPackage source) {
        super(source);
    }
}
