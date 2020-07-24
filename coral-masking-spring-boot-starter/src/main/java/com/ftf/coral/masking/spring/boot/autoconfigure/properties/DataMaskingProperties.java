package com.ftf.coral.masking.spring.boot.autoconfigure.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coral.masking")
public class DataMaskingProperties {

    private boolean enabled = true;

    private Map<String, String> ruleFunction;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getRuleFunction() {
        return ruleFunction;
    }

    public void setRuleFunction(Map<String, String> ruleFunction) {
        this.ruleFunction = ruleFunction;
    }
}
