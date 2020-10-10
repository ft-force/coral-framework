package com.ftf.coral.admin.business.prest.dto;

import javax.validation.constraints.NotNull;

public class UpdateScAccountRoleRequest {

    @NotNull(message = "accountId cannot be null")
    protected Long accountId;

    private String roleGroup;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }
}