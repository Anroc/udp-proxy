package com.anroc.mitm.proxy.bindings.in.events;

import com.anroc.mitm.proxy.bindings.PackageReceivedEvent;
import com.anroc.mitm.proxy.data.UDPPackage;

public class ClientPackageReceivedEvent extends PackageReceivedEvent {
    public ClientPackageReceivedEvent(UDPPackage source) {
        super(source);
    }
}
