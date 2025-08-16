package com.web.demo.dto.Request;

import java.io.Serializable;
import java.math.BigDecimal;


import jakarta.validation.constraints.NotNull;

public class WithdrawRequestDto implements Serializable {

    private static final long serialVersionUID = 1008L;

        @NotNull
        private BigDecimal transactionAmount;


        public WithdrawRequestDto() {

        }

        public WithdrawRequestDto(BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
            
        }

        public BigDecimal getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

      
    
}
