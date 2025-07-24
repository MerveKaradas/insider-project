package com.web.demo.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.web.demo.service.abstracts.AuditLogService;
import com.web.demo.util.GlobalContext;
import com.web.demo.event.AuditEvent;
import com.web.demo.model.AuditLog;

@Component
public class AuditEventListener {

    private final AuditLogService auditLogService;

    public AuditEventListener(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @TransactionalEventListener
    public void handleAuditEvent(AuditEvent event) {
        auditLogService.saveAuditLog(
            event.getEntityType(),
            event.getEntityId(),
            event.getAction(),
            event.getDetails(),
            event.getPerformedBy(),
            event.getIpAddress(),
            event.getUserAgent()
        );
    }

    
}
