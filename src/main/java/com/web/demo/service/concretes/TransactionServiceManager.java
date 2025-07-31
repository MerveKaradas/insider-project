package com.web.demo.service.concretes;

import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.web.demo.service.abstracts.TransactionService;
import com.web.demo.strategy.TransactionStrategy;
import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;
import com.web.demo.model.TransactionType;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.repository.abstracts.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
public class TransactionServiceManager implements TransactionService {

    private final Map<String, TransactionStrategy> strategyMap;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceManager(List<TransactionStrategy> strategies, UserRepository userRepository,TransactionRepository transactionRepository) {
       this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(
                s -> s.getType().name(), // enum döndürüyoruz
                Function.identity()
            ));
            this.userRepository = userRepository;
            this.transactionRepository = transactionRepository;
       
    }

    public Transaction executeTransaction(TransactionRequestDto request, String email) {
        
        // Strategy seçilir
        TransactionType type = request.getTransactionType();
        TransactionStrategy strategy = strategyMap.get(type.name());

        if (strategy == null) {
            throw new UnsupportedOperationException("İşlem tipi desteklenmiyor: " + type);
        }

       userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Kullanici bulunamdi"));

        // işleme yönlendirme
        return strategy.processTransaction(request);


    }

    @Override
    public List<Transaction> getHistory(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadi"));

        return transactionRepository.findAllByFromUserId_IdOrToUserId_IdOrderByTransactionsCreatedAtDesc(user.getId(), user.getId());
    
    }


    @Override
    public Transaction findByIdIfBelongsToUser(Long transactionId, User user) {

        user = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadi"));

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("İşlem bulunamadı"));

        if (!transaction.getFromUserId().getId().equals(user.getId()) &&
            !transaction.getToUserId().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bu işleme erişim yetkiniz yok");
        }

        return transaction;
}


    

  


    
 
}
