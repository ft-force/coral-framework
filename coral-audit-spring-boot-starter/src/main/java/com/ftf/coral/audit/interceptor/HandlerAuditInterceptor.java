package com.ftf.coral.audit.interceptor;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ftf.coral.audit.AuditLogManager;
import com.ftf.coral.audit.annotation.Audit;
import com.ftf.coral.audit.model.HttpRequestAuditLog;
import com.ftf.coral.audit.util.IPUtils;
import com.ftf.coral.util.StringUtils;

public class HandlerAuditInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
                    throws Exception {

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Audit audit = handlerMethod.getMethodAnnotation(Audit.class);

            if (audit == null) {
                // 无需进行审计处理
                return;
            }

            HttpRequestAuditLog auditLog = new HttpRequestAuditLog();
            auditLog.setResourceType(audit.resourceType());
            if (!StringUtils.isBlank(audit.eventType())) {
                auditLog.setEventType(audit.eventType());
            } else {
                auditLog.setEventType(request.getRequestURI());
            }
            auditLog.setEventTime(new Date());
            auditLog.setRemoteAddr(IPUtils.getRemoteIP(request));
            auditLog.setHttpMethod(request.getMethod());
            auditLog.setRequestURI(request.getRequestURI());
            auditLog.setRequestQueryString(request.getQueryString());
            auditLog.setRequestHeaders(Collections.list(request.getHeaderNames()).stream()
                            .collect(Collectors.toMap(h -> h, request::getHeader)));
            auditLog.setRequestBody(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
            auditLog.setResponseStatus(response.getStatus());
            auditLog.setException(ex);

            AuditLogManager.dealAuditLog(auditLog);
        }
    }
}
