package com.ftf.coral.auth.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ftf.coral.auth.interceptor.HandlerAppAuthInterceptor;
import com.ftf.coral.auth.spring.boot.autoconfigure.properties.AuthProperties;
import com.ftf.coral.core.auth.signer.DefaultSigner;
import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;

@Configuration
@EnableConfigurationProperties({ AuthProperties.class })
@ConditionalOnProperty(prefix = "coral.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(CoralAutoConfiguration.class)
public class AuthAutoConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditAutoConfiguration.class);

    AuthProperties properties;

    public AuthAutoConfiguration(AuthProperties properties) {

        this.properties = properties;
        LOGGER.info("认证模块已开启");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry
                        .addInterceptor(new HandlerAppAuthInterceptor(new DefaultSigner()));

        if (properties.getPathPatterns() != null && properties.getPathPatterns().length > 0) {
            registration.addPathPatterns(properties.getPathPatterns());
        }

        if (properties.getExcludePathPatterns() != null && properties.getExcludePathPatterns().length > 0) {
            registration.excludePathPatterns(properties.getExcludePathPatterns());
        }
    }
}
