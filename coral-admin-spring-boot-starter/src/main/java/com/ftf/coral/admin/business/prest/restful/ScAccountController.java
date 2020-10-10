package com.ftf.coral.admin.business.prest.restful;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Spring;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ftf.coral.admin.business.app.service.LdapService;
import com.ftf.coral.admin.business.app.service.ScAccountService;
import com.ftf.coral.admin.business.infra.mapper.ScAccountMapper;
import com.ftf.coral.admin.business.infra.mapper.ScAccountRoleMapper;
import com.ftf.coral.admin.business.prest.dto.CreateScAccountRequest;
import com.ftf.coral.admin.business.prest.dto.ScAccountDTO;
import com.ftf.coral.admin.business.prest.dto.UpdateScAccountRequest;
import com.ftf.coral.admin.business.prest.dto.UpdateScAccountRoleRequest;
import com.ftf.coral.admin.core.ScAccountManager;
import com.ftf.coral.admin.core.ScToken;
import com.ftf.coral.admin.core.ScTokenManager;
import com.ftf.coral.admin.core.annotation.ScAccountAuth;
import com.ftf.coral.admin.core.entity.ScAccount;
import com.ftf.coral.admin.core.session.ScTokenSession;
import com.ftf.coral.business.model.ResponseDTO;
import com.ftf.coral.core.page.PageData;
import com.ftf.coral.core.page.PageRequest;
import com.ftf.coral.util.HttpRequestUtils;
import com.ftf.coral.util.IPUtil;
import com.ftf.coral.util.StringUtils;

@RestController
public class ScAccountController extends BaseController {

    @Autowired
    private LdapService ldapService;
    @Autowired
    private ScAccountService scAccountService;
    @Autowired
    private ScAccountMapper scAccountMapper;
    @Autowired
    private ScAccountRoleMapper scAccountRoleMapper;
    @Autowired
    private ScTokenSession scTokenSession;

    @ScAccountAuth
    @GetMapping("/scaccounts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<PageData<Map<Spring, Object>>> pageQurey(
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(value = "username", required = false) String username) {

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(pageNum);
        pageRequest.setSize(pageSize);
        if (StringUtils.isNotBlank(username)) {
            pageRequest.getConditionMap().put("username", username);
        }

        return new ResponseDTO<PageData<Map<Spring, Object>>>().success(scAccountService.pageQuery(pageRequest));
    }

    @ScAccountAuth({"admin"})
    @PostMapping("/scaccounts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<Long> create(@Valid @RequestBody CreateScAccountRequest createScAccountRequest) {

        ScAccount scAccount = new ScAccount();
        scAccount.setCategory(1);// AD
        scAccount.setUsername(createScAccountRequest.getUsername());

        Long id = scAccountService.createScAccount(scAccount, createScAccountRequest.getRoleGroup());

        return new ResponseDTO<Long>().success(id);
    }

    @ScAccountAuth({"admin"})
    @DeleteMapping("/scaccounts/{accountId:.+}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<Boolean> deleteScAccount(@PathVariable Long accountId) {

        return new ResponseDTO<Boolean>().success(scAccountService.deleteScAccount(accountId));
    }

    @ScAccountAuth({"admin"})
    @PutMapping("/scaccounts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<?> update(@Valid @RequestBody UpdateScAccountRequest updateScAccountRequest) {

        ScAccount scAccount = new ScAccount();
        scAccount.setId(updateScAccountRequest.getId());
        scAccount.setStatus(updateScAccountRequest.getStatus());

        int rows = scAccountService.updateScAccount(scAccount);

        if (rows > 0) {
            return new ResponseDTO<>().success();
        } else {
            return new ResponseDTO<>().failure("data not found");
        }
    }

    @ScAccountAuth({"admin"})
    @PutMapping("/scaccounts/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<?> updateRole(@Valid @RequestBody UpdateScAccountRoleRequest updateScAccountRoleRequest) {

        scAccountService.updateScAccountRole(updateScAccountRoleRequest.getAccountId(),
            updateScAccountRoleRequest.getRoleGroup());

        return new ResponseDTO<>().success();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<Boolean> login(@Valid @RequestBody ScAccountDTO accountDTO, HttpServletRequest request,
        HttpServletResponse response) {

        // if (!ldapService.authenticate(accountDTO.getUserName(), accountDTO.getPassword())) {
        // return new ResponseDTO<Boolean>().failure("该账号不存在或密码错误");
        // }

        ScAccount scAccount = scAccountMapper.selectScAccount(accountDTO.getUserName());

        if (scAccount != null) {
            if (scAccount.getStatus() == 0) { // Normal
                List<String> roles = scAccountRoleMapper.selectRoleCodeList(scAccount.getId());

                ScToken scToken = ScTokenManager.getCurrentScToken();
                scToken.upgrade(scAccount.getId().toString());

                String clientIp = IPUtil.getClientIp(request);
                String accessToken = scToken.accessToken(clientIp);

                scTokenSession.init(scToken, scAccount, roles);

                // 写入 cookie
                String topDomain = HttpRequestUtils.getTopDomain(request);
                javax.servlet.http.Cookie d = new javax.servlet.http.Cookie("_sa_a", accessToken);
                d.setDomain(topDomain);
                d.setHttpOnly(false);
                d.setPath("/");
                response.addCookie(d);

                return new ResponseDTO<Boolean>().success(true);
            } else {
                return new ResponseDTO<Boolean>().failure("账号状态异常，禁止登录");
            }
        }

        return new ResponseDTO<Boolean>().failure("用户信息不存在");
    }

    @ScAccountAuth
    @GetMapping("/get_info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<ScAccount> getUserInfo() {

        return new ResponseDTO<ScAccount>().success(scAccountMapper
            .selectScAccount(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername()));
    }

    @ScAccountAuth
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {

        // 登出操作
        scTokenSession.clear(ScTokenManager.getCurrentScToken());

        // 写入 cookie
        String topDomain = HttpRequestUtils.getTopDomain(request);
        javax.servlet.http.Cookie d = new javax.servlet.http.Cookie("_sa_a", null);
        d.setDomain(topDomain);
        d.setPath("/");
        d.setMaxAge(0);
        response.addCookie(d);

        return new ResponseDTO<Boolean>().success(true);
    }
}