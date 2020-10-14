package com.ftf.coral.admin.business.app.service;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LdapService {

    private static final Logger LOGGERR = LoggerFactory.getLogger(LdapService.class);

    @Value("${ldap.url}")
    protected String ldapUrl;
    @Value("${ldap.domainName}")
    private String ldapDomainName;

    public boolean authenticate(String username, String password) throws NamingException { // 身份认证

        String userDomainName = String.format(ldapDomainName, username);

        LdapContext ctx = null;
        Hashtable<String, String> HashEnv = new Hashtable<String, String>();
        HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong)
        HashEnv.put(Context.SECURITY_PRINCIPAL, userDomainName); // AD的用户名
        HashEnv.put(Context.SECURITY_CREDENTIALS, password); // AD的密码
        HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");// 连接超时设置为3秒
        HashEnv.put(Context.PROVIDER_URL, ldapUrl);// 默认端口389
        try {
            ctx = new InitialLdapContext(HashEnv, null);// new InitialDirContext(HashEnv);// 初始化上下文
            return true;
        } catch (AuthenticationException e) {
            LOGGERR.warn(e.getMessage());
            return false;
        } finally {
            if (null != ctx) {
                try {
                    ctx.close();
                    ctx = null;
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }
}