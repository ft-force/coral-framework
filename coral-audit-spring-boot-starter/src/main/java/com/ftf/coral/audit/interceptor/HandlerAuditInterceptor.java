package com.ftf.coral.audit.interceptor;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ftf.coral.audit.AuditLogManager;
import com.ftf.coral.audit.annotation.Audit;
import com.ftf.coral.audit.model.HttpRequestAuditLog;
import com.ftf.coral.audit.util.IPUtils;
import com.ftf.coral.util.StringUtils;
import com.ftf.coral.util.SystemClock;

public class HandlerAuditInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger("http-access-log");

    private ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        startTimeHolder.set(SystemClock.now());

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {

        // 打印访问日志
        LOGGER.info("[{}][{} {}?{} {}] {} {}ms", IPUtils.getRemoteIP(request), request.getMethod(),
            request.getRequestURI(), request.getQueryString(), request.getProtocol(), response.getStatus(),
            SystemClock.now() - startTimeHolder.get(), ex);

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod)handler;

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
            auditLog.setEventId(request.getHeader("x-request-id")); // 事件ID
            auditLog.setStartTime(startTimeHolder.get());
            auditLog.setTimeTaken(SystemClock.now() - startTimeHolder.get()); // 耗时
            auditLog.setRemoteAddr(IPUtils.getRemoteIP(request));
            auditLog.setHttpMethod(request.getMethod());
            auditLog.setRequestURI(request.getRequestURI());
            auditLog.setRequestQueryString(request.getQueryString());
            auditLog.setRequestHeaders(Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(h -> h, request::getHeader)));
            auditLog.setRequestBody(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
            auditLog.setResponseStatus(response.getStatus());
            auditLog.setException(ex);

            startTimeHolder.remove();

            AuditLogManager.dealAuditLog(auditLog);
        }
    }
}
