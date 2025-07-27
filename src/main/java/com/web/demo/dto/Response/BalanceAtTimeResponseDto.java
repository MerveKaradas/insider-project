package com.web.demo.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BalanceAtTimeResponseDto {
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime atTime;

    public BalanceAtTimeResponseDto(Long userId, BigDecimal amount, LocalDateTime atTime) {
        this.userId = userId;
        this.amount = amount;
        this.atTime = atTime;
    }

    // Getter ve Setterlar
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getAtTime() { return atTime; }
    public void setAtTime(LocalDateTime atTime) { this.atTime = atTime; }

    public BigDecimal getAmount() {
        return amount;
    }
}
