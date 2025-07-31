package com.web.demo.service.abstracts;

import java.util.List;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;
import com.web.demo.model.User;

public interface TransactionService {

    Transaction executeTransaction(TransactionRequestDto request, String email);

    List<Transaction> getHistory(String email);

    Transaction findByIdIfBelongsToUser(Long id, User user);

  
    
}
