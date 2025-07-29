package com.web.demo.dto.Response;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;


public class BalanceResponseDto implements Serializable {

    private static final long serialVersionUID = 2002L;

    private Long balanceId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime balancesLastUpdatedAt;
    private LocalDateTime balancesCreatedAt;

    public BalanceResponseDto() {
    }

    public BalanceResponseDto(Long balanceId, Long userId, BigDecimal amount, LocalDateTime balancesLastUpdatedAt,
            LocalDateTime balancesCreatedAt) {
        this.balanceId = balanceId;
        this.userId = userId;
        this.amount = amount;
        this.balancesLastUpdatedAt = balancesLastUpdatedAt;
        this.balancesCreatedAt = balancesCreatedAt;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getBalancesLastUpdatedAt() {
        return balancesLastUpdatedAt;
    }

    public void setBalancesLastUpdatedAt(LocalDateTime balancesLastUpdatedAt) {
        this.balancesLastUpdatedAt = balancesLastUpdatedAt;
    }

    public LocalDateTime getBalancesCreatedAt() {
        return balancesCreatedAt;
    }

    public void setBalancesCreatedAt(LocalDateTime balancesCreatedAt) {
        this.balancesCreatedAt = balancesCreatedAt;
    }

    
    
    
}
