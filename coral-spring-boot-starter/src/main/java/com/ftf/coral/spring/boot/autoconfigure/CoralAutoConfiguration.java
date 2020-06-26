package com.ftf.coral.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.ftf.coral.core.runtime.spring.context.ApplicationContextSupport;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CoralAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApplicationContextSupport.class)
    public ApplicationContextSupport applicationContextSupport() {
        return new ApplicationContextSupport();
    }
}
