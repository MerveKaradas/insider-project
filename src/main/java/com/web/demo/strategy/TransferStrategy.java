package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.service.abstracts.UserService;

import org.springframework.stereotype.Service;

@Service("TRANSFER") // Transfer işlemi
public class TransferStrategy implements TransactionStrategy {


    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransferStrategy(BalanceRepository balanceRepository, TransactionRepository transactionRepository,
            UserService userService) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Override
    public Transaction processTransaction(TransactionRequestDto request) {
        // Transfer işlemi için gerekli adımlar:
        // 1. Gönderen kullanıcının bakiyesini kontrol et
        // 2. Alıcı kullanıcının bakiyesini güncelle
        // 3. Gönderen kullanıcının bakiyesinden tutarı düş
        // 4. Veritabanına kaydet


    }
    
}
