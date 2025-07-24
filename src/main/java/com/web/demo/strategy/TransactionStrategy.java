package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;

public interface TransactionStrategy {
    void processTransaction(TransactionRequestDto request);
}
