package com.web.demo.service.validation;


import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Balance;
import com.web.demo.repository.abstracts.BalanceRepository;

@Service
public class WithdrawValidationService {

    private final BalanceRepository balanceRepository;

    public WithdrawValidationService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public void validateWithdrawRequest(TransactionRequestDto request) {
        if (request.getTransactionAmount() == null || request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Çekilecek tutar pozitif olmalıdır.");
        }
    }

    public void validateSufficientBalance(Balance balance, BigDecimal withdrawAmount) {
        if (balance.getBalancesAmount().compareTo(withdrawAmount) < 0) {
            throw new IllegalArgumentException("Yetersiz bakiye.");
        }
    }

    public Balance validateAndGetUserBalance(Long userId, String errorMessageIfNotFound) {
        return balanceRepository.findBalanceByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(errorMessageIfNotFound));
    }
}