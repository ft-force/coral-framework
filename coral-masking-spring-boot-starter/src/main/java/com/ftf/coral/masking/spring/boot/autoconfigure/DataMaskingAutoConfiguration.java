package com.ftf.coral.masking.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ftf.coral.masking.spring.DataMaskingMethodScanner;
import com.ftf.coral.masking.spring.boot.autoconfigure.properties.DataMaskingProperties;
import com.ftf.coral.spring.boot.autoconfigure.CoralAutoConfiguration;

@Configuration
@EnableConfigurationProperties({ DataMaskingProperties.class })
@ConditionalOnProperty(prefix = "coral.masking", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(CoralAutoConfiguration.class)
public class DataMaskingAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DataMaskingAutoConfiguration.class);

    public DataMaskingAutoConfiguration(DataMaskingProperties properties) {

        logger.info("数据脱敏模块已开启");
    }

    @Bean
    @ConditionalOnMissingBean(DataMaskingMethodScanner.class)
    public DataMaskingMethodScanner dataMaskingMethodScanner(DataMaskingProperties properties) {
        return new DataMaskingMethodScanner(properties != null ? properties.getRuleFunction() : null);
    }
}
