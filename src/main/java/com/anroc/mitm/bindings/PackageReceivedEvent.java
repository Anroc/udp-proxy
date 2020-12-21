package com.anroc.mitm.bindings;

import com.anroc.mitm.MITMEvent;
import com.anroc.mitm.proxy.data.UDPPackage;
import com.anroc.mitm.proxy.events.PackageProcessedEvent;
import org.springframework.context.ApplicationEvent;

public abstract class PackageReceivedEvent extends MITMEvent<UDPPackage> {

    public PackageReceivedEvent(UDPPackage source) {
        super(source);
    }
}
