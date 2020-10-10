package com.ftf.coral.admin.business.prest.restful;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ftf.coral.admin.business.infra.mapper.ScApplicationAdminMapper;
import com.ftf.coral.admin.core.ScAccountManager;
import com.ftf.coral.admin.protobuf.ScAccountInfo;
import com.ftf.coral.util.CollectionUtils;

public class BaseController {

    @Autowired
    private ScApplicationAdminMapper scApplicationAdminMapper;

    protected List<String> queryApplicationIdsByAccountId(Long accountId) {
        return scApplicationAdminMapper.selectByAccountId(accountId);
    }

    protected boolean hasPermission(String applicationId) {

        ScAccountInfo scAccountInfo = ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo();

        if (CollectionUtils.isNotEmpty(scAccountInfo.getRolesList())) {

            if (scAccountInfo.getRolesList().contains("admin")) {
                return true;
            }
        }

        List<String> applicationIds = this.queryApplicationIdsByAccountId(scAccountInfo.getAccountId().getValue());

        if (CollectionUtils.isNotEmpty(applicationIds)) {

            if (applicationIds.contains(applicationId)) {
                return true;
            }
        }

        return false;
    }

    protected void noPermission(HttpServletResponse response) throws IOException {
        response.setHeader("ftf-event-code", "Platform.NoPermission");
        response.setHeader("ftf-event-type", "LoginRequired");
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}