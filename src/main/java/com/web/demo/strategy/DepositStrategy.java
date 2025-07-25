package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.service.abstracts.UserService;
import com.web.demo.model.User;
import com.web.demo.model.Transaction;
import com.web.demo.model.Balance;
import com.web.demo.model.TransactionType;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.repository.abstracts.UserRepository;
import com.web.demo.model.TransactionStatus; 
import java.math.BigDecimal;

@Service("DEPOSIT")
public class DepositStrategy implements TransactionStrategy {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public DepositStrategy(BalanceRepository balanceRepository,
                           TransactionRepository transactionRepository,
                           UserService userService,
                           UserRepository userRepository) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Transaction processTransaction(TransactionRequestDto request) {

        // Kullanıcıyı bul
        User user = userService.findById(request.getFromUserId());

        // Mevcut bakiyeyi al
        Balance balance = balanceRepository.findBalanceByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bakiyesi bulunamadı"));

        // Yeni bakiyeyi hesapla
        BigDecimal newBalance = balance.getBalancesAmount().add(request.getTransactionAmount());
        balance.setBalancesAmount(newBalance);
        balanceRepository.save(balance); 

        User systemUser = userRepository.findByUsername("system").orElseThrow();
   
        // Transaction oluştur ve kaydet
        Transaction transaction = new Transaction();
        transaction.setFromUserId(systemUser); 
        transaction.setToUserId(user);
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction); 
    }
}