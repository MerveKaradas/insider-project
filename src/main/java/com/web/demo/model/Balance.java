package com.web.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balances")
@EntityListeners(AuditingEntityListener.class) //entity nesnelerinde otomatik olarak tarih/saat gibi izleme alanlarını doldurmak için kullanılan dinleyicidir.JPA tarafından sağlanır.
public class Balance {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long balancesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User balancesUserId;

    @Column(name = "balances_amount", nullable = false)
    private BigDecimal balancesAmount;


    @UpdateTimestamp
    @Column(name = "balances_last_updated_at", nullable = false)
    private LocalDateTime balancesLastUpdatedAt;

    public Balance() {
    }

    public Balance(User balancesUserId, BigDecimal balancesAmount) {
        this.balancesUserId = balancesUserId;
        this.balancesAmount = balancesAmount;
    }

    public Long getBalancesId() {
        return balancesId;
    }

    public void setBalancesId(Long balancesId) {
        this.balancesId = balancesId;
    }

    public User getBalancesUserId() {
        return balancesUserId;
    }

    public void setBalancesUserId(User balancesUserId) {
        this.balancesUserId = balancesUserId;
    }

    public BigDecimal getBalancesAmount() {
        return balancesAmount;
    }

    public void setBalancesAmount(BigDecimal balancesAmount) {
        this.balancesAmount = balancesAmount;
    }

    public LocalDateTime getBalancesLastUpdatedAt() {
        return balancesLastUpdatedAt;
    }

    public void setBalancesLastUpdatedAt(LocalDateTime balancesLastUpdatedAt) {
        this.balancesLastUpdatedAt = balancesLastUpdatedAt;
    }

    

    
}
