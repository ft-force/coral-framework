package com.ftf.coral.lock.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.ftf.coral.lock.DistributedLock;
import com.ftf.coral.lock.redis.RedisDistributedLock;
import com.ftf.coral.lock.spring.boot.autoconfigure.properties.LockProperties;
import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;

@Configuration
@EnableConfigurationProperties({ LockProperties.class })
@ConditionalOnProperty(prefix = "coral.lock", name = "enabled", havingValue = "true")
@AutoConfigureAfter({ CoralAutoConfiguration.class, RedisAutoConfiguration.class })
public class LockAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockAutoConfiguration.class);

    LockProperties properties;

    public LockAutoConfiguration(LockProperties properties) {

        this.properties = properties;
        LOGGER.info("分布式锁模块已开启");
    }

    @Bean
    public DistributedLock distributedLock(StringRedisTemplate redisTemplate) {

        return new RedisDistributedLock(redisTemplate);
    }
}
