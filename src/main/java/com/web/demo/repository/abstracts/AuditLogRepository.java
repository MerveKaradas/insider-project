package com.web.demo.repository.abstracts;

import com.web.demo.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
     

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    
}
