package com.ftf.coral.audit;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftf.coral.audit.handler.AuditLogHandler;
import com.ftf.coral.audit.handler.HttpRequestAuditLogHandler;
import com.ftf.coral.audit.model.AuditLog;
import com.ftf.coral.audit.model.HttpRequestAuditLog;

public class AuditLogManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogManager.class);

    @SuppressWarnings("rawtypes")
    private static ConcurrentHashMap<Class, AuditLogHandler> auditLogHandlerMap = new ConcurrentHashMap<>();

    static {
        AuditLogManager.registAuditLogHandler(HttpRequestAuditLog.class, new HttpRequestAuditLogHandler());
    }

    public static <T extends AuditLog> void registAuditLogHandler(Class<T> logType, AuditLogHandler<T> logHandler) {
        auditLogHandlerMap.put(logType, logHandler);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AuditLog> AuditLogHandler<T> getAuditLogHandler(Class<T> logType) {
        return auditLogHandlerMap.get(logType);
    }

    /**
     * 异步处理审计数据 可添加自定义处理器
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void dealAuditLog(AuditLog auditLog) {

        AuditLogHandler handler = getAuditLogHandler(auditLog.getClass());

        if (handler != null) {
            handler.dealAuditLog(auditLog);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("未发现审计日志处理器");
            }
        }
    }
}
