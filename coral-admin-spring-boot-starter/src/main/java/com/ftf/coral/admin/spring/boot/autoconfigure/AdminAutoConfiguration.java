package com.ftf.coral.admin.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ftf.coral.admin.core.CoralAdminCore;
import com.ftf.coral.admin.core.session.ScTokenSession;
import com.ftf.coral.admin.spring.boot.autoconfigure.properties.AdminProperties;
import com.ftf.coral.admin.spring.interceptor.HandlerScTokenInterceptor;
import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;

@Configuration
@EnableConfigurationProperties({AdminProperties.class})
@ConditionalOnProperty(prefix = "coral.admin", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(CoralAutoConfiguration.class)
public class AdminAutoConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAutoConfiguration.class);

    AdminProperties properties;

    public AdminAutoConfiguration(AdminProperties properties) {

        this.properties = properties;
        CoralAdminCore.setTokenPrefix(properties.getTokenPrefix());
        LOGGER.info("Admin模块已开启");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(handlerScTokenInterceptor());

        if (properties.getPathPatterns() != null && properties.getPathPatterns().length > 0) {
            registration.addPathPatterns(properties.getPathPatterns());
        }

        if (properties.getExcludePathPatterns() != null && properties.getExcludePathPatterns().length > 0) {
            registration.excludePathPatterns(properties.getExcludePathPatterns());
        }
    }

    @Bean
    public ScTokenSession scTokenSession(RedisTemplate<String, byte[]> redisTemplate) {
        return new ScTokenSession(redisTemplate);
    }

    @Bean
    public HandlerScTokenInterceptor handlerScTokenInterceptor() {
        return new HandlerScTokenInterceptor();
    }
}