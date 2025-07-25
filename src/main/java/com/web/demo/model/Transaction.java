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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;


 @Entity
 @EntityListeners(AuditingEntityListener.class) //entity nesnelerinde otomatik olarak tarih/saat gibi izleme alanlarını doldurmak için kullanılan dinleyicidir.JPA tarafından sağlanır.
 @Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactions_id", nullable = false, updatable = false)
    private Long transactionsId;

    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY, ilişkili verilerin yalnızca ihtiyaç duyulduğunda yüklenmesini sağlar.
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUserId;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @CreationTimestamp
    @Column(name = "transaction_created_at", nullable = false, updatable = false)
    private LocalDateTime transactionsCreatedAt;


    public Transaction() {
    }

 
    public Transaction(User fromUserId, User to_userId, BigDecimal transactions_amount, TransactionType type, TransactionStatus status) {
        this.fromUserId = fromUserId;
        this.toUserId = to_userId;
        this.transactionAmount = transactions_amount;
        this.type = type;
        this.status = status;
    }




    public Long getTransactionsId() {
        return transactionsId;
    }


    public void setTransactionsId(Long transactionsId) {
        this.transactionsId = transactionsId;
    }


    public User getFromUserId() {
        return fromUserId;
    }


    public void setFromUserId(User from_userId) {
        this.fromUserId = from_userId;
    }


    public User getToUserId() {
        return toUserId;
    }


    public void setToUserId(User to_userId) {
        this.toUserId = to_userId;
    }


    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactions_amount) {
        this.transactionAmount = transactions_amount;
    }


    public TransactionType getType() {
        return type;
    }


    public void setType(TransactionType type) {
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
