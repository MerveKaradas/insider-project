package com.web.demo.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.web.demo.service.abstracts.AuditLogService;
import com.web.demo.event.AuditEvent;

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
