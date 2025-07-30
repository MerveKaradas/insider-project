package com.web.demo.service.concretes;

import com.web.demo.service.abstracts.AuditLogService;
import com.web.demo.repository.abstracts.AuditLogRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.model.AuditLog;

@Service
public class AuditLogServiceManager implements AuditLogService {

    
   private final AuditLogRepository auditLogRepository;

    public AuditLogServiceManager(AuditLogRepository auditLogRepository) {
         this.auditLogRepository = auditLogRepository;
    }


    
    public void saveAuditLog(String entityType, Long entityId, String action, String details, String performedBy, String ipAddress, String userAgent) {
       
        AuditLog auditLog = new AuditLog();

        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setPerformedBy(performedBy);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);
        
        auditLogRepository.save(auditLog);
        
    }
    
    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"could not serialize\"}";
        }
    }
}
