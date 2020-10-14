package com.ftf.coral.admin.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ftf.coral.admin.core.CoralAdminCore;
import com.ftf.coral.admin.core.ScAccountManager;
import com.ftf.coral.admin.core.ScToken;
import com.ftf.coral.admin.core.ScTokenManager;
import com.ftf.coral.admin.core.annotation.ScAccountAuth;
import com.ftf.coral.admin.core.session.ScTokenSession;
import com.ftf.coral.admin.protobuf.ScAccountInfo;
import com.ftf.coral.admin.protobuf.ScTokenSessionInfo;
import com.ftf.coral.business.context.UserContext;
import com.ftf.coral.util.CollectionUtils;
import com.ftf.coral.util.HttpRequestUtils;
import com.ftf.coral.util.IPUtil;

public class HandlerScTokenInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerScTokenInterceptor.class);

    @Autowired
    private ScTokenSession scTokenSession;

    public HandlerScTokenInterceptor() {}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        if ("options".equalsIgnoreCase(request.getMethod())) {
            return super.preHandle(request, response, handler);
        }

        String token = null;
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (CollectionUtils.isNotEmpty(cookies)) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals(CoralAdminCore.getTokenKey("a"))) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        String clientIp = IPUtil.getClientIp(request);
        LOGGER.debug("ClientIp: {}", clientIp);

        ScToken tk = null;
        if (token != null && token.trim().length() > 0) {
            tk = ScToken.parse(token.trim());
            if (tk != null && (tk.isValidate(clientIp) == false)) {
                tk = null;
            }

            if (tk != null) {
                // 获取 TokenSessionInfo
                ScTokenSessionInfo tokenSessionInfo = scTokenSession.getScTokenSessionInfo(tk);
                if (tokenSessionInfo != null) {
                    ScAccountManager.putCurrentTokenSessionInfo(tokenSessionInfo);
                } else {
                    // session 已经失效
                    tk = null;
                }
            }
        }

        boolean isNewToken = false;
        if (tk == null) {
            tk = ScToken.generateToken(null);
            token = tk.accessToken(clientIp);
            isNewToken = true;
        }

        ScTokenManager.putCurrentScToken(tk);

        if (isNewToken) {
            ScTokenSessionInfo tokenSessionInfo = scTokenSession.init(tk, null, null);
            ScAccountManager.putCurrentTokenSessionInfo(tokenSessionInfo);

            // 写入 cookie
            String topDomain = HttpRequestUtils.getTopDomain(request);
            javax.servlet.http.Cookie d = new javax.servlet.http.Cookie(CoralAdminCore.getTokenKey("a"), token);
            d.setDomain(topDomain);
            d.setHttpOnly(false);
            d.setPath("/");
            response.addCookie(d);

        } else {
            scTokenSession.refresh(tk);
        }

        if (tk.isLogin()) {
            UserContext.setCurrentUser(ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo().getUsername());
        }

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerTarget = (HandlerMethod)handler;

            ScAccountAuth scAccountAuth = handlerTarget.getMethod().getAnnotation(ScAccountAuth.class);

            if (scAccountAuth != null) { // 需要进行Account认证

                if (!tk.isLogin()) {
                    // 未登录
                    response.setHeader("ftf-event-code", "Platform.NoSession");
                    response.setHeader("ftf-event-type", "LoginRequired");
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                    return false;
                } else {
                    if (CollectionUtils.isNotEmpty(scAccountAuth.value())) {
                        // 校验角色
                        ScAccountInfo scAccountInfo = ScAccountManager.getCurrentTokenSessionInfo().getScAccountInfo();

                        if (scAccountInfo.getRolesList().isEmpty()) {
                            response.setHeader("ftf-event-code", "Platform.NoPermission");
                            response.setHeader("ftf-event-type", "LoginRequired");
                            response.sendError(HttpStatus.UNAUTHORIZED.value());
                            return false;
                        } else {
                            for (String authRole : scAccountAuth.value()) {
                                if (scAccountInfo.getRolesList().contains(authRole)) {
                                    return true;
                                }
                            }

                            response.setHeader("ftf-event-code", "Platform.NoPermission");
                            response.setHeader("ftf-event-type", "LoginRequired");
                            response.sendError(HttpStatus.UNAUTHORIZED.value());
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}