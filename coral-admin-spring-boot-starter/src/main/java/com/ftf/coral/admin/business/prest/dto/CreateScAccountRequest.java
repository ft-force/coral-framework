package com.ftf.coral.admin.business.prest.dto;

import javax.validation.constraints.NotBlank;

public class CreateScAccountRequest {

    @NotBlank(message = "username cannot be blank")
    private String username;

    @NotBlank(message = "roleGroup cannot be blank")
    private String roleGroup;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }
}