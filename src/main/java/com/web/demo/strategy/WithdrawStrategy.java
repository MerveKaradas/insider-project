package com.web.demo.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Balance;
import com.web.demo.model.Transaction;
import com.web.demo.model.TransactionStatus;
import com.web.demo.model.TransactionType;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.service.abstracts.UserService;
import com.web.demo.service.validation.WithdrawValidationService;

@Service("WITHDRAW") //para çekme
public class WithdrawStrategy implements TransactionStrategy {

    private final WithdrawValidationService validationService;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
  

    public WithdrawStrategy(WithdrawValidationService validationService, BalanceRepository balanceRepository,
            TransactionRepository transactionRepository, UserService userService) {
        this.validationService = validationService;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAWAL;
    }

    @Transactional
    @Override
    public Transaction processTransaction(TransactionRequestDto request) {

        // Kullanıcı bulma
        User user = userService.findById(request.getFromUserId()); 
        User systemUser = userService.findByUsername("system");
        
         // Mevcut bakiyeyi alma
        Balance balance = validationService.validateAndGetUserBalance(
            user.getId(), "Kullanıcı bakiyesi bulunamadı");
    
        try {
            
            validationService.validateWithdrawRequest(request);
            validationService.validateSufficientBalance(balance, request.getTransactionAmount());

            
            // Yeni bakiye hesaplama
            BigDecimal newBalance = balance.getBalancesAmount().subtract(request.getTransactionAmount());
            balance.setBalancesAmount(newBalance);
            balanceRepository.save(balance); 

            // Transaction oluşturma (Başarılı)
            Transaction transaction = new Transaction();
            transaction.setFromUserId(user); 
            transaction.setToUserId(systemUser);
            transaction.setTransactionAmount(request.getTransactionAmount());
            transaction.setType(TransactionType.WITHDRAWAL);
            transaction.setStatus(TransactionStatus.SUCCESS);
            
            return transactionRepository.save(transaction); 

        } catch(Exception e) {

            // Başarısız işlem
            Transaction failedTransaction = new Transaction();
            failedTransaction.setFromUserId(user); 
            failedTransaction.setToUserId(systemUser);
            failedTransaction.setTransactionAmount(request.getTransactionAmount());
            failedTransaction.setType(TransactionType.WITHDRAWAL);
            failedTransaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(failedTransaction); 

            
           if (e instanceof IllegalArgumentException) throw e;

            e.printStackTrace(); // TODO : loglama 
            throw new RuntimeException("Para çekme işlemi sırasında beklenmedik bir hata oluştu");
        }



















        


        
        
    }
    
}
