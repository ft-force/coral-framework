package com.ftf.coral.runtime.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class PropertiesUtils implements EmbeddedValueResolverAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    private static StringValueResolver valueResolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        valueResolver = stringValueResolver;
    }

    public static String getPropertiesValue(String key, boolean required) {

        StringBuilder name = new StringBuilder("${").append(key).append("}");

        try {
            return valueResolver.resolveStringValue(name.toString());
        } catch (Throwable e) {
            if (required) {
                LOGGER.warn(e.getMessage());
                throw e;
            }
            return null;
        }
    }

    public static String getPropertiesValue(String key) {

        return getPropertiesValue(key, true);
    }
}