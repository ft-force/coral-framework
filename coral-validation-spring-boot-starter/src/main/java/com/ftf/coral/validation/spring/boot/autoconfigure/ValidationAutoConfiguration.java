package com.ftf.coral.validation.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;
import com.ftf.coral.validation.spring.boot.autoconfigure.properties.ValidationProperties;

@Configuration
@EnableConfigurationProperties({ValidationProperties.class})
@ConditionalOnProperty(prefix = "coral.validation", name = "enabled", havingValue = "true")
@AutoConfigureAfter({CoralAutoConfiguration.class})
public class ValidationAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAutoConfiguration.class);

    ValidationProperties properties;

    public ValidationAutoConfiguration(ValidationProperties properties) {

        this.properties = properties;
        LOGGER.info("数据校验模块已开启");
    }
}
