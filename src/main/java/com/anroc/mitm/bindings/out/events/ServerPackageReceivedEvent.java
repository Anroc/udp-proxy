package com.anroc.mitm.bindings.out.events;

import com.anroc.mitm.bindings.PackageReceivedEvent;
import com.anroc.mitm.proxy.data.UDPPackage;

public class ServerPackageReceivedEvent extends PackageReceivedEvent {
    public ServerPackageReceivedEvent(UDPPackage source) {
        super(source);
    }
}
