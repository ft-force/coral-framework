package com.ftf.coral.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ftf.coral.auth.annotation.AppAuth;
import com.ftf.coral.core.auth.signer.Signer;

public class HandlerAppAuthInterceptor extends HandlerInterceptorAdapter {

    private Signer signer;

    public HandlerAppAuthInterceptor(Signer signer) {
        this.signer = signer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {

        if ("options".equalsIgnoreCase(request.getMethod())) {
            return super.preHandle(request, response, handler);
        }

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerTarget = (HandlerMethod) handler;

            AppAuth appAuth = handlerTarget.getMethod().getAnnotation(AppAuth.class);

            if (appAuth != null) { // 需要进行APP认证

                boolean verifyResult = signer.verify(new HttpServletRequestWapper(request));

                if (!verifyResult) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed.");
                    return verifyResult;
                }
            }
        }

        return Boolean.TRUE;
    }
}
