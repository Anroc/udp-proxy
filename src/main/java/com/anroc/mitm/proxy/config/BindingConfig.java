package com.anroc.mitm.proxy.config;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
@Configuration
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
