package com.ftf.coral.admin.business.app.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftf.coral.admin.business.infra.mapper.ScAccountMapper;
import com.ftf.coral.admin.business.infra.mapper.ScAccountRoleMapper;
import com.ftf.coral.admin.core.ScAccountManager;
import com.ftf.coral.admin.core.entity.ScAccount;
import com.ftf.coral.admin.core.entity.ScAccountRole;
import com.ftf.coral.core.exception.BusinessException;
import com.ftf.coral.core.exception.ResourceNotExistException;
import com.ftf.coral.core.page.PageData;
import com.ftf.coral.core.page.PageRequest;

@Service
public class ScAccountService {

    private static final Logger LOGGERR = LoggerFactory.getLogger(ScAccountService.class);

    @Autowired
    private ScAccountMapper scAccountMapper;
    @Autowired
    private ScAccountRoleMapper scAccountRoleMapper;

    public Boolean exists(String username) {
        return scAccountMapper.existByUsername(username);
    }

    public PageData<Map<String, Object>> pageQuery(PageRequest pageRequest) {

        List<Map<String, Object>> scAccountList = scAccountMapper.pageSelect(pageRequest);

        return new PageData<>(pageRequest, pageRequest.getTotalItems(), scAccountList);
    }

    @Transactional
    public Long createScAccount(ScAccount scAccount, String roleGroup) {

        if (scAccountMapper.existByUsername(scAccount.getUsername())) {
            throw new BusinessException("ScAccount already exists. Username = " + scAccount.getUsername());
        }

        scAccount.setStatus(0);// Normal
        scAccount.setCreationUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
        scAccount.setLastModifiedUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
        scAccountMapper.save(scAccount);

        String[] roleArr = roleGroup.split(",");

        for (String role : roleArr) {
            ScAccountRole scAccountRole = new ScAccountRole();
            scAccountRole.setAccountId(scAccount.getId());
            scAccountRole.setRoleCode(role);
            scAccountRole
                .setCreationUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
            scAccountRole
                .setLastModifiedUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
            scAccountRoleMapper.save(scAccountRole);
        }

        return scAccount.getId();
    }

    public int updateScAccount(ScAccount scAccount) {

        scAccount.setLastModifiedUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());

        return scAccountMapper.update(scAccount);
    }

    @Transactional
    public Boolean deleteScAccount(Long id) {

        int rows = scAccountMapper.deleteById(id);

        if (rows > 0) {
            scAccountRoleMapper.deleteByAccountId(id);

            return true;
        } else {
            throw new ResourceNotExistException("ScAccount not exists. Id = " + id);
        }
    }

    @Transactional
    public void updateScAccountRole(Long accountId, String roleGroup) {

        this.scAccountRoleMapper.deleteByAccountId(accountId);

        String[] roleArr = roleGroup.split(",");

        for (String role : roleArr) {
            ScAccountRole scAccountRole = new ScAccountRole();
            scAccountRole.setAccountId(accountId);
            scAccountRole.setRoleCode(role);
            scAccountRole
                .setCreationUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
            scAccountRole
                .setLastModifiedUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
            scAccountRoleMapper.save(scAccountRole);
        }
    }
}