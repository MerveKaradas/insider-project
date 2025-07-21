package com.web.demo.model;

import com.web.demo.model.TransactionsType;

import jakarta.persistence.Id;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.web.demo.model.TransactionStatus;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


// @Entity
// @EntityListeners(AuditingEntityListener.class) //entity nesnelerinde otomatik olarak tarih/saat gibi izleme alanlarını doldurmak için kullanılan dinleyicidir.JPA tarafından sağlanır.
// @Table(name = "transactions")
public class Transactions {

  
    // @Column(nullable = false, updatable = false)
    // private Long id;


    // private Long from_user_id;

    // @Column(nullable = false)
    // private Long to_user_id;

    // @Column(nullable = false)
    // private Double amount;

    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private TransactionsType type;

    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private TransactionStatus status;

    // @CreationTimestamp
    // @Column(name = "created_at" ,nullable = false, updatable = false)
    // private LocalDateTime createdAt;

    



    
    
    
}
