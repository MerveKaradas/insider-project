package com.web.demo.service.concretes;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.web.demo.service.abstracts.TransactionService;
import com.web.demo.strategy.TransactionStrategy;
import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.TransactionType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
public class TransactionServiceManager implements TransactionService {

    private final Map<String, TransactionStrategy> strategyMap;

    public TransactionServiceManager(List<TransactionStrategy> strategies) {
       this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(
                s -> s.getType().name(), // enum döndürüyoruz
                Function.identity()
            ));
    }

    public void executeTransaction(TransactionRequestDto request) {
        TransactionType type = request.getTransactionType();
        TransactionStrategy strategy = strategyMap.get(type.name());

        if (strategy == null) {
            throw new UnsupportedOperationException("İşlem tipi desteklenmiyor: " + type);
        }

        strategy.processTransaction(request);
    }




    
 
}
