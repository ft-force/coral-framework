package com.ftf.coral.admin.business.app.service;

import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftf.coral.admin.business.infra.mapper.ScApplicationAdminMapper;
import com.ftf.coral.admin.core.ScAccountManager;
import com.ftf.coral.admin.core.entity.ScApplicationAdmin;
import com.ftf.coral.core.exception.BusinessException;
import com.ftf.coral.core.exception.ResourceNotExistException;
import com.ftf.coral.core.page.PageData;
import com.ftf.coral.core.page.PageRequest;

@Service
public class ScApplicationAdminService {

    private static final Logger LOGGERR = LoggerFactory.getLogger(ScApplicationAdminService.class);

    @Autowired
    private ScApplicationAdminMapper scApplicationAdminMapper;

    public PageData<Map<Spring, Object>> pageQuery(PageRequest pageRequest) {

        List<Map<Spring, Object>> scAccountList = scApplicationAdminMapper.pageSelect(pageRequest);

        return new PageData<>(pageRequest, pageRequest.getTotalItems(), scAccountList);
    }

    public Long createScApplicationAdmin(ScApplicationAdmin scApplicationAdmin) {

        if (scApplicationAdminMapper.exist(scApplicationAdmin.getApplicationId(), scApplicationAdmin.getAccountId())) {
            throw new BusinessException("ScApplicationAdmin already exists. ApplicationId = "
                + scApplicationAdmin.getApplicationId() + ";AccountId = " + scApplicationAdmin.getAccountId());
        }

        scApplicationAdmin
            .setCreationUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
        scApplicationAdmin
            .setLastModifiedUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
        scApplicationAdminMapper.save(scApplicationAdmin);

        return scApplicationAdmin.getId();
    }

    public Boolean deleteScApplicationAdmin(Long id) {

        int rows = scApplicationAdminMapper.deleteById(id);

        if (rows > 0) {
            return true;
        } else {
            throw new ResourceNotExistException("ScApplicationAdmin not exists. Id = " + id);
        }
    }
}