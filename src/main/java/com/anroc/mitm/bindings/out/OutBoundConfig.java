package com.anroc.mitm.bindings.out;

import com.anroc.mitm.bindings.BindingConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "out-bound")
public class OutBoundConfig extends BindingConfig {
}
