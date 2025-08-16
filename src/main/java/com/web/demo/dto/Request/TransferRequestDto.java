package com.web.demo.dto.Request;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class TransferRequestDto implements Serializable {

    private static final long serialVersionUID = 1009L;

    @NotNull
    private Long toUserId;

    @NotNull
    private BigDecimal transactionAmount;

    public TransferRequestDto() {
    }

    public TransferRequestDto(Long toUserId, BigDecimal transactionAmount) {
        this.toUserId = toUserId;
        this.transactionAmount = transactionAmount;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    
    
}
