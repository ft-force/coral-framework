package com.ftf.coral.admin.business.app.service;

import java.util.List;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import com.ftf.coral.core.ldap.User;

@Service
public class LdapService {

    private static final Logger LOGGERR = LoggerFactory.getLogger(LdapService.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${ldap.domainName}")
    private String ldapDomainName;
    @Value("${ldap.base}")
    private String ldapBaseDn;

    public List<User> getPersonList(String ldapBase, Filter filter) { // 获取用户列表

        return ldapTemplate.search(ldapBase, filter.encode(), new AttributesMapper<User>() {

            @Override
            public User mapFromAttributes(Attributes attr) throws NamingException, javax.naming.NamingException {
                User person = new User();
                String distingugihedName = (String)attr.get("distinguishedName").get();
                person.setUsername((String)attr.get("username").get());
                person.setEmail((String)attr.get("mail").get());
                person.setRealname((String)attr.get("name").get());
                if (null != attr.get("mobile")) {
                    person.setMobile((String)attr.get("mobile").get());
                }
                if (null != attr.get("telephoneNumber")) {
                    person.setPhone((String)attr.get("telephoneNumber").get());
                }
                person.setLdapflag(1);
                String departmentName = StringUtils.substringAfter(distingugihedName.split(",")[1], "OU=");
                person.setUnitname(departmentName);
                return person;
            }
        });
    }

    public boolean authenticate(String username, String password) { // 身份认证

        String userDomainName = String.format(ldapDomainName, username);

        DirContext ctx = null;

        try {

            ctx = ldapTemplate.getContextSource().getContext(userDomainName, password);
            return true;

        } finally {
            LdapUtils.closeContext(ctx);
        }
    }
}
