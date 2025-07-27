package com.web.demo.service.validation;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Balance;
import com.web.demo.repository.abstracts.BalanceRepository;

@Service
public class TransactionValidationService {

     private final BalanceRepository balanceRepository;

    public TransactionValidationService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public void validateTransferRequest(TransactionRequestDto request) {
        if (request.getTransactionAmount() == null || request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer miktarı pozitif olmalıdır.");
        }

        if (request.getFromUserId().equals(request.getToUserId())) {
            throw new IllegalArgumentException("Kendinize para transferi yapamazsınız.");
        }
    }

    public void validateSufficientBalance(Balance fromUserBalance, BigDecimal transferAmount) {
        if (fromUserBalance.getBalancesAmount().compareTo(transferAmount) < 0) {
            throw new IllegalArgumentException("Yetersiz bakiye.");
        }
    }

    public Balance validateAndGetUserBalance(Long userId, String errorMessageIfNotFound) {
        return balanceRepository.findByBalancesUserId_Id(userId)
                .orElseThrow(() -> new IllegalArgumentException(errorMessageIfNotFound));
    }
}