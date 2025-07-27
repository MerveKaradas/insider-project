package com.web.demo.dto.Response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.web.demo.model.TransactionType;
import com.web.demo.model.TransactionStatus;

public class TransactionResponseDto implements Serializable {

    private static final long serialVersionUID = 2004L;

    private Long transactionId;
    private Long fromUserId;
    private Long toUserId;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionTime;
    private TransactionStatus transactionStatus;
    private TransactionType transactionType;

    public TransactionResponseDto(){
        
    }


    public TransactionResponseDto(Long transactionId, Long fromUserId, Long toUserId, BigDecimal transactionAmount,
            LocalDateTime transactionTime, TransactionStatus transactionStatus, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.transactionType = transactionType;
    }


    public Long getTransactionId() {
        return transactionId;
    }


    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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


    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }


    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }


    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }


    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


    public TransactionType getTransactionType() {
        return transactionType;
    }


    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    
  

    

    
}
