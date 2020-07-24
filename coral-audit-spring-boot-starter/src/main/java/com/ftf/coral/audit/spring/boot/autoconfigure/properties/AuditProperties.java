package com.ftf.coral.audit.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coral.audit")
public class AuditProperties {

    private boolean enabled = true;

    private String[] pathPatterns;

    private String[] excludePathPatterns;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String[] getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(String[] pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public String[] getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(String[] excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }
}
