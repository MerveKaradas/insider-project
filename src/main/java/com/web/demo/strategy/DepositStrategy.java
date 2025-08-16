package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.event.AuditEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.service.abstracts.UserService;
import com.web.demo.service.validation.DepositValidationService;
import com.web.demo.util.GlobalContext;
import com.web.demo.model.User;
import com.web.demo.model.Transaction;
import com.web.demo.model.Balance;
import com.web.demo.model.TransactionType;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.model.TransactionStatus; 
import java.math.BigDecimal;

@Service("DEPOSIT") //para yatırma
public class DepositStrategy implements TransactionStrategy {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final DepositValidationService validationService;
    private final AuditEventPublisher auditEventPublisher;

    public DepositStrategy(BalanceRepository balanceRepository,
                           TransactionRepository transactionRepository,
                           UserService userService,
                           DepositValidationService validationService,
                           AuditEventPublisher auditEventPublisher) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.validationService = validationService;
        this.auditEventPublisher = auditEventPublisher;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
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
        User user = userService.findById(request.getToUserId()); 
        User systemUser = userService.findByUsername("system");

        System.out.println("Gelen kullanici id : " + user.getId() +  " " + user.getEmail() + "  balanceuserid : " + balanceRepository.findByBalancesUserId_Id(request.getFromUserId()));
        
         // Mevcut bakiyeyi alma
        Balance balance = validationService.validateAndGetUserBalance(
            user.getId(), "Kullanıcı bakiyesi bulunamadı");

        try {

            BigDecimal newBalance = balance.getBalancesAmount().add(request.getTransactionAmount());
            balance.setBalancesAmount(newBalance);
            balanceRepository.save(balance); 

            Transaction transaction = new Transaction();
            transaction.setFromUserId(systemUser); 
            transaction.setToUserId(user);
            transaction.setTransactionAmount(request.getTransactionAmount());
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setStatus(TransactionStatus.SUCCESS);

            transaction = transactionRepository.save(transaction);
            logger(transaction,"DEPOSIT","Hesaba para yatırma işlemi başarılı bir şekilde gerçekleşti");

            return transaction; 

        } catch(Exception e) {
            // Başarısız işlem
            Transaction failedTransaction = new Transaction();
            failedTransaction.setFromUserId(systemUser); 
            failedTransaction.setToUserId(user);
            failedTransaction.setTransactionAmount(request.getTransactionAmount());
            failedTransaction.setType(TransactionType.DEPOSIT);
            failedTransaction.setStatus(TransactionStatus.FAILED);

            transactionRepository.save(failedTransaction); 

            
           if (e instanceof IllegalArgumentException) throw e;
            e.printStackTrace(); 

            logger(failedTransaction,"DEPOSIT","Hesaba para yatırma işlemi başarısız oldu");
            throw new RuntimeException("Para yatırma işlemi sırasında beklenmedik bir hata oluştu");
            
        }
 
        
            

        
    }
}