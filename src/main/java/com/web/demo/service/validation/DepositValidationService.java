package com.web.demo.service.validation;

import org.springframework.stereotype.Service;

import com.web.demo.model.Balance;
import com.web.demo.repository.abstracts.BalanceRepository;

@Service
public class DepositValidationService {

     private final BalanceRepository balanceRepository;

     

     public DepositValidationService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

     public Balance validateAndGetUserBalance(Long userId, String errorMessageIfNotFound) {
        return balanceRepository.findBalanceByBalancesUserId_Id(userId)
                .orElseThrow(() -> new IllegalArgumentException(errorMessageIfNotFound));
    }
    
}
