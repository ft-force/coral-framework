package com.ftf.coral.audit.handler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftf.coral.audit.model.HttpRequestAuditLog;
import com.ftf.coral.util.DateUtils;

public class HttpRequestAuditLogHandler implements AuditLogHandler<HttpRequestAuditLog> {

    private static final Logger LOGGER = LoggerFactory.getLogger("http-audit-log");

    @Override
    public void dealAuditLog(HttpRequestAuditLog auditLog) {

        // @formatter:off
        LOGGER.info("[ResourceType:{}][EventType:{} EventId:{} StartTime:{} TimeTaken:{}ms][{} {}?{} {}][{}][{}]", 
            auditLog.getResourceType(),
            auditLog.getEventType(), auditLog.getEventId(), DateUtils.getSimpleDateString(new Date(auditLog.getStartTime())), auditLog.getTimeTaken(),
            auditLog.getHttpMethod(), auditLog.getRequestURI(), auditLog.getRequestQueryString(), auditLog.getHttpProtocol(),
            auditLog.getResponseStatus(), auditLog.getRemoteAddr(), auditLog.getException());
        // @formatter:on
    }
}