package com.ftf.coral.core.ldap;

public class User {

    private String username;
    private String realname;
    private String email;
    private String mobile;
    private String phone;
    private Integer ldapflag;
    private String unitname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLdapflag() {
        return ldapflag;
    }

    public void setLdapflag(Integer ldapflag) {
        this.ldapflag = ldapflag;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }
}