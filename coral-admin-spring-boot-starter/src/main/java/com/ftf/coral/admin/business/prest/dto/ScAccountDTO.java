package com.ftf.coral.admin.business.prest.dto;

import javax.validation.constraints.NotBlank;

public class ScAccountDTO {

    @NotBlank(message = "userName cannot be blank")
    private String userName;

    @NotBlank(message = "password cannot be blank")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
