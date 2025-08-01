package com.web.demo.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.event.AuditEventPublisher;
import com.web.demo.model.Balance;
import com.web.demo.model.Transaction;
import com.web.demo.model.TransactionStatus;
import com.web.demo.model.TransactionType;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.service.abstracts.UserService;
import com.web.demo.service.validation.WithdrawValidationService;
import com.web.demo.util.GlobalContext;

@Service("WITHDRAW") //para çekme
public class WithdrawStrategy implements TransactionStrategy {

    private final WithdrawValidationService validationService;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AuditEventPublisher auditEventPublisher;
  

    public WithdrawStrategy(WithdrawValidationService validationService, 
            BalanceRepository balanceRepository,
            TransactionRepository transactionRepository, 
            UserService userService,
            AuditEventPublisher auditEventPublisher) {
        this.validationService = validationService;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.auditEventPublisher = auditEventPublisher;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAWAL;
    }

     public void logger(Transaction transaction,String action, String details){
        // Audit loglama
        auditEventPublisher.publish(
            "Transaction", 
            transaction.getTransactionsId(), 
            action, 
            details,
            GlobalContext.getCurrentUsername(),
            GlobalContext.getIpAddress(),
            GlobalContext.getUserAgent());
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

            transaction = transactionRepository.save(transaction);
            logger(transaction,"WITHDRAWAL","Hesaptan para çekme işlemi başarılı bir şekilde gerçekleşti");

            return transaction; 

        } catch(Exception e) {

            // Başarısız işlem
            Transaction failedTransaction = new Transaction();
            failedTransaction.setFromUserId(user); 
            failedTransaction.setToUserId(systemUser);
            failedTransaction.setTransactionAmount(request.getTransactionAmount());
            failedTransaction.setType(TransactionType.WITHDRAWAL);
            failedTransaction.setStatus(TransactionStatus.FAILED);

            transactionRepository.save(failedTransaction); 
            logger(failedTransaction,"WITHDRAWAL","Hesaptan para çekme işlemi başarısız oldu");
           
           if (e instanceof IllegalArgumentException) throw e;
           
            e.printStackTrace(); 
            throw new RuntimeException("Para çekme işlemi sırasında beklenmedik bir hata oluştu");
        }



















        


        
        
    }
    
}
