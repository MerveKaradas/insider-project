package com.web.demo.event;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;

/*
 * KATMAN                GÖREV
-------------------------------------------------------
AuditLog Entity       → DB’ye yazılacak asıl veri (JPA @Entity)
AuditLogService       → Log işleme servisi
AuditEventPublisher   → Event’i fırlatıyor (publish)
AuditLogListener      → Event’i dinleyip AuditLog kaydı yapıyor
AuditEvent            → Fırlatılan olayın taşıyıcısı (event payload)
GlobalContext         → IP, userAgent, kullanıcı bilgisi

AuditLogRepository    → AuditLog kayıtlarını DB’ye yazmak için kullanılıyor
 * 
 */


public class AuditEvent {
    
    private String entityType;
    private Long entityId;
    private String action;
    private String details;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String performedBy;
    private String ipAddress;
    private String userAgent;

    public AuditEvent() {
    }

    public AuditEvent(String entityType, Long entityId, String action, String details, String performedBy, String ipAddress, String userAgent) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public String getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }


}
