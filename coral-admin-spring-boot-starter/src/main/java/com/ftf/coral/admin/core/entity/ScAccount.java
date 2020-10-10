package com.ftf.coral.admin.core.entity;

import com.ftf.coral.business.entity.BaseEntity;

public class ScAccount extends BaseEntity {

    private String username;

    private Integer category;
    private Integer status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}