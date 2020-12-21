package com.anroc.mitm.proxy.events;

import com.anroc.mitm.MITMEvent;
import com.anroc.mitm.proxy.data.UDPPackage;

public abstract class PackageProcessedEvent extends MITMEvent<UDPPackage> {
    public PackageProcessedEvent(UDPPackage source) {
        super(source);
    }
}
