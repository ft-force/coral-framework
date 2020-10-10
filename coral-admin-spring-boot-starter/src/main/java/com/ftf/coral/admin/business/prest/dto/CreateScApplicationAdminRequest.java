package com.ftf.coral.admin.business.prest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateScApplicationAdminRequest {

    @NotBlank(message = "applicationId cannot be blank")
    private String applicationId;

    @NotNull(message = "username cannot be null")
    private String username;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}