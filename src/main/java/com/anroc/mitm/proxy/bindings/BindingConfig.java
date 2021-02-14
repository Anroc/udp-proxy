package com.anroc.mitm.proxy.bindings;

import lombok.Data;
import lombok.SneakyThrows;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.net.InetAddress;

@Data
public abstract class BindingConfig {

    @NotBlank
    private String address;
    @Positive
    private int port;

    @SneakyThrows
    public InetAddress getInetAddress() {
        return InetAddress.getByName(address);
    }
}
