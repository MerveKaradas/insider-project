package com.web.demo.dto.Request;

import java.math.BigDecimal;
import com.web.demo.model.TransactionType;


public class TransactionRequestDto {

    private Long fromUserId;
    private Long toUserId;
    private BigDecimal transactionAmount;
    private TransactionType transactionType;
   

    public TransactionRequestDto() {
    }

    public TransactionRequestDto(Long fromUserId, Long toUserId, BigDecimal transactionAmount, TransactionType transactionType) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
       
    }
    

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    
}
