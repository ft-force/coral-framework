package com.ftf.coral.auth.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ftf.coral.auth.spring.boot.autoconfigure.properties.AuthProperties;
import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;

@Configuration
@EnableConfigurationProperties({ AuthProperties.class })
@ConditionalOnProperty(prefix = "coral.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(CoralAutoConfiguration.class)
public class AuthAutoConfiguration {

}
