package com.web.demo.dto.Response;

import java.time.LocalDateTime;

public class BalanceAtTimeResponseDto {
    private Long userId;
    private Double amount;
    private LocalDateTime atTime;

    public BalanceAtTimeResponseDto(Long userId, Double amount, LocalDateTime atTime) {
        this.userId = userId;
        this.amount = amount;
        this.atTime = atTime;
    }

    // Getter ve Setterlar
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getAtTime() { return atTime; }
    public void setAtTime(LocalDateTime atTime) { this.atTime = atTime; }
}
