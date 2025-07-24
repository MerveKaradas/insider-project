package com.web.demo.model;

import jakarta.persistence.Id;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

 @Entity
 @EntityListeners(AuditingEntityListener.class) //entity nesnelerinde otomatik olarak tarih/saat gibi izleme alanlarını doldurmak için kullanılan dinleyicidir.JPA tarafından sağlanır.
 @Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactions_id", nullable = false, updatable = false)
    private Long transactionsId;


    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from_userId;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to_userId;

    @Column(name = "transactions_amount", nullable = false)
    private Double transactions_amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionsType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @CreationTimestamp
    @Column(name = "transaction_created_at", nullable = false, updatable = false)
    private LocalDateTime transactionsCreatedAt;


    public Transaction() {
    }

 
    public Transaction(User from_userId, User to_userId, Double transactions_amount, TransactionsType type, TransactionStatus status) {
        this.from_userId = from_userId;
        this.to_userId = to_userId;
        this.transactions_amount = transactions_amount;
        this.type = type;
        this.status = status;
    }


    public Long getTransactionsId() {
        return transactionsId;
    }


    public void setTransactionsId(Long transactionsId) {
        this.transactionsId = transactionsId;
    }


    public User getFrom_userId() {
        return from_userId;
    }


    public void setFrom_userId(User from_userId) {
        this.from_userId = from_userId;
    }


    public User getTo_userId() {
        return to_userId;
    }


    public void setTo_userId(User to_userId) {
        this.to_userId = to_userId;
    }


    public Double getTransactions_amount() {
        return transactions_amount;
    }


    public void setTransactions_amount(Double transactions_amount) {
        this.transactions_amount = transactions_amount;
    }


    public TransactionsType getType() {
        return type;
    }


    public void setType(TransactionsType type) {
        this.type = type;
    }


    public TransactionStatus getStatus() {
        return status;
    }


    public void setStatus(TransactionStatus status) {
        this.status = status;
    }


    public LocalDateTime getTransactionsCreatedAt() {
        return transactionsCreatedAt;
    }


    public void setTransactionsCreatedAt(LocalDateTime transactionsCreatedAt) {
        this.transactionsCreatedAt = transactionsCreatedAt;
    }

    


    
    
}
