package com.web.demo.dto.Request;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


public class DepositRequestDto implements Serializable {
    
        private static final long serialVersionUID = 1007L;

        @NotNull
        @DecimalMin(value = "0.01", message = "Yatırım tutarı 1'den fazla olmalıdır.")
        private BigDecimal transactionAmount;


        public DepositRequestDto() {

        }

        public DepositRequestDto(BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
            
        }

        public BigDecimal getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(BigDecimal transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

      

        
        
}
