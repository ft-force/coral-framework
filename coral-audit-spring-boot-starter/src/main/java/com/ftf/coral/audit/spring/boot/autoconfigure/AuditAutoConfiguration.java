package com.ftf.coral.audit.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ftf.coral.audit.interceptor.HandlerAuditInterceptor;
import com.ftf.coral.audit.spring.boot.autoconfigure.properties.AuditProperties;
import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;

@Configuration
@EnableConfigurationProperties({AuditProperties.class})
@ConditionalOnProperty(prefix = "coral.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(CoralAutoConfiguration.class)
public class AuditAutoConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditAutoConfiguration.class);

    AuditProperties properties;

    public AuditAutoConfiguration(AuditProperties properties) {

        this.properties = properties;
        LOGGER.info("审计模块已开启");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new HandlerAuditInterceptor());

        if (properties.getPathPatterns() != null && properties.getPathPatterns().length > 0) {
            registration.addPathPatterns(properties.getPathPatterns());
        }

        if (properties.getExcludePathPatterns() != null && properties.getExcludePathPatterns().length > 0) {
            registration.excludePathPatterns(properties.getExcludePathPatterns());
        }
    }
}
