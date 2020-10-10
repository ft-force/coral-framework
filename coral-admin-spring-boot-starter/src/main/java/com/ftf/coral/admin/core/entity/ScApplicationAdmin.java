package com.ftf.coral.admin.core.entity;

import com.ftf.coral.business.entity.BaseEntity;

public class ScApplicationAdmin extends BaseEntity {

    private String applicationId;
    private Long accountId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}