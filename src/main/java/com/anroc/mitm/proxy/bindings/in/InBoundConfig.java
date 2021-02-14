package com.anroc.mitm.proxy.bindings.in;

import com.anroc.mitm.proxy.bindings.BindingConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "in-bound")
public class InBoundConfig extends BindingConfig {
}
