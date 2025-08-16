package com.web.demo.strategy;

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
import com.web.demo.service.validation.TransactionValidationService;
import com.web.demo.util.GlobalContext;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TRANSFER") // Transfer işlemi
public class TransferStrategy implements TransactionStrategy {

    private final TransactionValidationService validationService;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
     private final AuditEventPublisher auditEventPublisher;

    public TransferStrategy(BalanceRepository balanceRepository, 
            TransactionRepository transactionRepository,
            UserService userService , 
            TransactionValidationService validationService,
            AuditEventPublisher auditEventPublisher) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.validationService = validationService;
        this.auditEventPublisher = auditEventPublisher;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
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

        // Kullanıcıyı bul
        User fromUser = userService.findById(request.getFromUserId());
        User toUser = userService.findById(request.getToUserId());

        // Mevcut bakiye
        Balance fromUserBalance = validationService.validateAndGetUserBalance(
            fromUser.getId(), "Gönderici kullanıcı bakiyesi bulunamadı");
        
        Balance toUserBalance = validationService.validateAndGetUserBalance(
                 toUser.getId(), "Alıcı kullanıcı bakiyesi bulunamadı");


         try {
           
            validationService.validateTransferRequest(request);
            validationService.validateSufficientBalance(fromUserBalance, request.getTransactionAmount());

            
            // Yeni bakiye hesabı
            BigDecimal newToUserBalance = toUserBalance.getBalancesAmount().add(request.getTransactionAmount());
            BigDecimal newFromUserBalance = fromUserBalance.getBalancesAmount().subtract(request.getTransactionAmount());

            fromUserBalance.setBalancesAmount(newFromUserBalance);
            toUserBalance.setBalancesAmount(newToUserBalance);
            
            balanceRepository.save(fromUserBalance);
            balanceRepository.save(toUserBalance);

            // Başarılı işlem
            Transaction transaction = new Transaction();
            transaction.setFromUser(fromUser); 
            transaction.setToUser(toUser);
            transaction.setTransactionAmount(request.getTransactionAmount());
            transaction.setType(TransactionType.TRANSFER);
            transaction.setStatus(TransactionStatus.SUCCESS);

            transaction = transactionRepository.save(transaction); 
            logger(transaction,"TRANSFER","Hesaptan hesaba para aktarma işlemi başarılı bir şekilde gerçekleşti");

            return transaction;

        } catch (Exception e) {
            // diğer beklenmedik hatalar

            Transaction failedTransaction = new Transaction();
            failedTransaction.setFromUser(fromUser); 
            failedTransaction.setToUser(toUser);
            failedTransaction.setTransactionAmount(request.getTransactionAmount());
            failedTransaction.setType(TransactionType.TRANSFER);
            failedTransaction.setStatus(TransactionStatus.FAILED);

            transactionRepository.save(failedTransaction); 
            logger(failedTransaction,"TRANSFER","Hesaptan hesaba para aktarma işlemi başarısız oldu");

           if (e instanceof IllegalArgumentException) throw e;

            e.printStackTrace(); // TODO : loglama yapılmalı burada
            throw new RuntimeException("Transfer sırasında beklenmedik bir hata oluştu");
        }


    }
    
}
