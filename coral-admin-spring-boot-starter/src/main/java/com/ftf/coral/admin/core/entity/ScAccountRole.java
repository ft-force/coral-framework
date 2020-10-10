package com.ftf.coral.admin.core.entity;

import com.ftf.coral.business.entity.BaseEntity;

public class ScAccountRole extends BaseEntity {

    private Long accountId;
    private String roleCode;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}