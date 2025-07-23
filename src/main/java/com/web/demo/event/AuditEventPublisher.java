package com.web.demo.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.web.demo.util.GlobalContext;
import com.web.demo.model.AuditLog;
import com.web.demo.event.AuditEvent;
import com.web.demo.util.GlobalContext;

@Component
public class AuditEventPublisher {

    private final ApplicationEventPublisher publisher;

    public AuditEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(String entityType, Long entityId, String action, String details, 
                        String performedBy, String ipAddress, String userAgent) {

        AuditEvent event = new AuditEvent(
            entityType, entityId, action, details,
            performedBy != null ? performedBy : GlobalContext.getCurrentUsername(),
            ipAddress != null ? ipAddress : GlobalContext.getIpAddress(),
            userAgent != null ? userAgent : GlobalContext.getUserAgent()
        );
        publisher.publishEvent(event);
    }
}
