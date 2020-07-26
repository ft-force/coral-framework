package com.ftf.coral.core.auth.credentials;

import com.ftf.coral.util.StringUtils;

public class BasicCredential implements Credential {

    private String appId;
    private String accessKeyId;
    private String secretAccessKey;

    public BasicCredential(String accessKeyId, String secretAccessKey) {
        this(null, accessKeyId, secretAccessKey);
    }

    public BasicCredential(String appId, String accessKeyId, String secretAccessKey) {
        if (accessKeyId == null) {
            throw new IllegalArgumentException("accessKeyId cannot be null.");
        }
        if (secretAccessKey == null) {
            throw new IllegalArgumentException("secretAccessKey cannot be null.");
        }

        if (appId != null) {
            this.appId = appId.trim();
        }
        this.accessKeyId = accessKeyId.trim();
        this.secretAccessKey = secretAccessKey.trim();
    }

    public String getAppId() {
        return appId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        BasicCredential other = (BasicCredential) obj;

        if (!StringUtils.equals(appId, other.getAppId())) {
            return false;
        }
        if (!StringUtils.equals(accessKeyId, other.getAccessKeyId())) {
            return false;
        }
        if (!StringUtils.equals(secretAccessKey, other.getSecretAccessKey())) {
            return false;
        }

        return true;
    }
}