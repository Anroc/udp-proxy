package com.anroc.mitm.bindings.in.events;

import com.anroc.mitm.bindings.PackageReceivedEvent;
import com.anroc.mitm.proxy.data.UDPPackage;

public class ClientPackageReceivedEvent extends PackageReceivedEvent {
    public ClientPackageReceivedEvent(UDPPackage source) {
        super(source);
    }
}
