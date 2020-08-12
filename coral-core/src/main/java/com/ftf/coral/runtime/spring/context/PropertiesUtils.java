package com.ftf.coral.runtime.spring.context;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class PropertiesUtils implements EmbeddedValueResolverAware {

    private static StringValueResolver valueResolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        valueResolver = stringValueResolver;
    }

    public static String getPropertiesValue(String name) {
        return valueResolver.resolveStringValue(name);
    }
}
