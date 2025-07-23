package com.web.demo.infrastructure.persistence.listener;

import jakarta.persistence.*;

public class AuditEntityListener {

    /*
        Anotasyon	    Ne zaman tetiklenir?
        @PrePersist	    Entity veritabanına eklenmeden önce
        @PostPersist	Entity veritabanına eklendikten sonra
        @PreUpdate	    Entity güncellenmeden önce
        @PostUpdate	    Entity güncellendikten sonra
        @PreRemove	    Entity silinmeden önce
        @PostRemove	    Entity silindikten sonra
     */

    @PrePersist
    public void beforeCreate(Object entity) {
        System.out.println("[AUDIT] Oluşturuluyor: " + entity);
    }

    @PostUpdate
    public void afterUpdate(Object entity) {
        System.out.println("[AUDIT] Güncellendi: " + entity);
    }

    @PostRemove
    public void afterDelete(Object entity) {
        System.out.println("[AUDIT] Silindi: " + entity);
    }
    
}
