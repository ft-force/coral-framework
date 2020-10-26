package com.ftf.coral.admin.business.prest.restful;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ftf.coral.admin.business.app.service.ScApplicationAdminService;
import com.ftf.coral.admin.business.infra.mapper.ScAccountMapper;
import com.ftf.coral.admin.business.infra.mapper.ScApplicationAdminMapper;
import com.ftf.coral.admin.business.prest.dto.CreateScApplicationAdminRequest;
import com.ftf.coral.admin.core.annotation.ScAccountAuth;
import com.ftf.coral.admin.core.entity.ScAccount;
import com.ftf.coral.admin.core.entity.ScApplicationAdmin;
import com.ftf.coral.business.model.ResponseDTO;
import com.ftf.coral.core.page.PageData;
import com.ftf.coral.core.page.PageRequest;
import com.ftf.coral.util.StringUtils;

@RestController
public class ScApplicationAdminController extends BaseController {

    @Autowired
    private ScApplicationAdminService scApplicationAdminService;
    @Autowired
    private ScAccountMapper scAccountMapper;
    @Autowired
    private ScApplicationAdminMapper scApplicationAdminMapper;

    @ScAccountAuth
    @GetMapping("/apps/admin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<PageData<Map<String, Object>>> pageQurey(
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(value = "applicationId") String applicationId,
        @RequestParam(value = "username", required = false) String username, HttpServletResponse response)
        throws IOException {

        if (!super.hasPermission(applicationId)) {
            super.noPermission(response);
            return null;
        }

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(pageNum);
        pageRequest.setSize(pageSize);
        pageRequest.getConditionMap().put("applicationId", applicationId);
        if (StringUtils.isNotBlank(username)) {
            pageRequest.getConditionMap().put("username", username);
        }
        return new ResponseDTO<PageData<Map<String, Object>>>()
            .success(scApplicationAdminService.pageQuery(pageRequest));
    }

    @ScAccountAuth
    @PostMapping("/apps/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<Long> create(@Valid @RequestBody CreateScApplicationAdminRequest createScApplicationAdminRequest,
        HttpServletResponse response) throws IOException {

        if (!super.hasPermission(createScApplicationAdminRequest.getApplicationId())) {
            super.noPermission(response);
            return null;
        }

        ScAccount scAccount = scAccountMapper.selectScAccount(createScApplicationAdminRequest.getUsername());

        if (scAccount == null) {
            return new ResponseDTO<Long>().failure("域账号不存在");
        }

        ScApplicationAdmin scApplicationAdmin = new ScApplicationAdmin();
        scApplicationAdmin.setApplicationId(createScApplicationAdminRequest.getApplicationId());
        scApplicationAdmin.setAccountId(scAccount.getId());

        Long id = scApplicationAdminService.createScApplicationAdmin(scApplicationAdmin);

        return new ResponseDTO<Long>().success(id);
    }

    @ScAccountAuth
    @DeleteMapping("/apps/admin/{id:.+}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<Boolean> deleteScApplicationAdmin(@PathVariable Long id, HttpServletResponse response)
        throws IOException {

        String applicationId = scApplicationAdminMapper.selectApplicationIdById(id);

        if (StringUtils.isNotBlank(applicationId)) {

            if (!super.hasPermission(applicationId)) {
                super.noPermission(response);
                return null;
            }

            return new ResponseDTO<Boolean>().success(scApplicationAdminService.deleteScApplicationAdmin(id));
        } else {
            return new ResponseDTO<Boolean>().failure("数据不存在");
        }
    }
}