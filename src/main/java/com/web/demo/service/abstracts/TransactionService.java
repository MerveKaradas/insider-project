package com.web.demo.service.abstracts;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;

public interface TransactionService {

    Transaction executeTransaction(TransactionRequestDto request, String email);

  
    
}
