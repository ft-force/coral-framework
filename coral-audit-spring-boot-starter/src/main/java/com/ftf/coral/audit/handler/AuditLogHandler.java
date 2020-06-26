package com.ftf.coral.audit.handler;

import com.ftf.coral.audit.model.AuditLog;

public interface AuditLogHandler<T extends AuditLog> {

    void dealAuditLog(T auditLog);
}
