package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Balance;
import com.web.demo.model.Transaction;
import com.web.demo.model.TransactionStatus;
import com.web.demo.model.TransactionType;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.repository.abstracts.TransactionRepository;

import org.springframework.stereotype.Service;

@Service("CREDIT") // kredi verme
public class CreditStrategy implements TransactionStrategy {

    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    

    public CreditStrategy(TransactionRepository transactionRepository,
            BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Transaction processTransaction(TransactionRequestDto request) {
        
   

        return new Transaction();
    }

    @Override
    public TransactionType getType() {
        return TransactionType.CREDIT;
    }
    
 
    
}
