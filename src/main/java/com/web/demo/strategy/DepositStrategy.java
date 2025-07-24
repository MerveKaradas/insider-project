package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import org.springframework.stereotype.Service;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.service.abstracts.UserService;
import com.web.demo.dto.Response.TransactionResponseDto;
import com.web.demo.mapper.BalanceMapper;
import com.web.demo.model.User;
import com.web.demo.model.Transaction;

import java.math.BigDecimal;

@Service("DEPOSIT") //para yatırma
public class DepositStrategy implements TransactionStrategy {

    private final BalanceRepository balanceRepository; // Örnek olarak, bakiyeyi yöneten bir repository
   

    public DepositStrategy(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }


    @Override
    public void processTransaction(TransactionRequestDto request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processTransaction'");
    }

   
    


}
