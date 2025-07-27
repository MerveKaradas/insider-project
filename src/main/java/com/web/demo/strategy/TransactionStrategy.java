package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;
import com.web.demo.model.TransactionType;

public interface TransactionStrategy {
    Transaction processTransaction(TransactionRequestDto request);
    TransactionType getType();

}
