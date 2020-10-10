package com.ftf.coral.admin.business.prest.dto;

import javax.validation.constraints.NotNull;

public class UpdateScAccountRequest {

    @NotNull(message = "id cannot be null")
    protected Long id;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}