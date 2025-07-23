package com.web.demo.service.abstracts;

public interface AuditLogService {

     void saveAuditLog(String entityType, Long entityId, String action, String details,
                   String performedBy, String ipAddress, String userAgent);
    
}
