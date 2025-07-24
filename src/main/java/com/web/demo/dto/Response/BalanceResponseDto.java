package com.web.demo.dto.Response;

import java.time.LocalDateTime;


public class BalanceResponseDto {

    private Long balanceId;
    private Long userId;
    private Double amount;
    private LocalDateTime lastUpdatedAt;

    public BalanceResponseDto() {
    }

    public BalanceResponseDto(Long balanceId, Long userId, Double amount, LocalDateTime lastUpdatedAt) {
        this.balanceId = balanceId;
        this.userId = userId;
        this.amount = amount;
        this.lastUpdatedAt = lastUpdatedAt;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
    
    
}
