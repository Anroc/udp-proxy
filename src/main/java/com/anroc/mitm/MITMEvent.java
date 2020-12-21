package com.anroc.mitm;

import org.springframework.context.ApplicationEvent;

public abstract class MITMEvent<T> extends ApplicationEvent {

    public MITMEvent(T source) {
        super(source);
    }

    public T getSource() {
        return (T) super.getSource();
    }
}

