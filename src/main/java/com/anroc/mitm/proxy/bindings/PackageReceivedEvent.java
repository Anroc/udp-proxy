package com.anroc.mitm.proxy.bindings;

import com.anroc.mitm.proxy.events.MITMEvent;
import com.anroc.mitm.proxy.data.UDPPackage;

public abstract class PackageReceivedEvent extends MITMEvent<UDPPackage> {

    public PackageReceivedEvent(UDPPackage source) {
        super(source);
    }
}
