package com.web.demo.dto.Request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BalanceRequestDto implements Serializable{

    private static final long serialVersionUID = 1006L;

    private Long userId;
    private BigDecimal amount;
    private LocalDateTime balancesLastUpdatedAt;
    private LocalDateTime balancesCreatedAt;

    public BalanceRequestDto() {
    }
    

    public BalanceRequestDto(Long userId, BigDecimal amount, LocalDateTime balancesLastUpdatedAt,
            LocalDateTime balancesCreatedAt) {
        this.userId = userId;
        this.amount = amount;
        this.balancesLastUpdatedAt = balancesLastUpdatedAt;
        this.balancesCreatedAt = balancesCreatedAt;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getBalancesLastUpdatedAt() {
        return balancesLastUpdatedAt;
    }

    public LocalDateTime getBalancesCreatedAt() {
        return balancesCreatedAt;
    }

    

    
}
