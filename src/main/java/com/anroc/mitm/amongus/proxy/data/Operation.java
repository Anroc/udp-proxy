package com.anroc.mitm.amongus.proxy.data;

public enum Operation {

    POSITION   ((byte) 0),
    UPDATE_GAME((byte) 1),
    CREATE_GAME((byte) 8), // can also be join game
    CANCLE     ((byte) 9),
    ACK        ((byte) 10),
    PING       ((byte) 12);


    private final byte code;

    Operation(byte code) {
        this.code = code;
    }
}
