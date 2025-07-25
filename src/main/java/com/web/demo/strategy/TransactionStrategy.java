package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;

public interface TransactionStrategy {
    Transaction processTransaction(TransactionRequestDto request);
}
