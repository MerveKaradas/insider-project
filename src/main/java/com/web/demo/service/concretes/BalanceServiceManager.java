package com.web.demo.service.concretes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import com.web.demo.service.abstracts.BalanceService;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.dto.Response.BalanceAtTimeResponseDto;
import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.dto.Response.HistoricalBalanceResponseDto;
import com.web.demo.mapper.BalanceMapper;
import com.web.demo.model.Balance;
import com.web.demo.repository.abstracts.TransactionRepository;
import java.util.List;

@Service
public class BalanceServiceManager implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;


    public BalanceServiceManager(BalanceRepository balanceRepository, TransactionRepository transactionRepository) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
      
    }

    @Override
    public BalanceResponseDto currentBalance(Long userId) {
       
        Balance balance = balanceRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Balance not found for user ID: " + userId));

        return BalanceMapper.toDto(balance);
    }

    @Override
    public HistoricalBalanceResponseDto historicalBalance(Long userId, LocalDateTime start, LocalDateTime end) {
        List<HistoricalBalanceResponseDto.Snapshot> snapshots = new ArrayList<>();

        LocalDateTime pointer = start;
        while (pointer.isBefore(end)) {
            Double totalIn = transactionRepository.sumToUserUntil(userId, pointer);
            Double totalOut = transactionRepository.sumFromUserUntil(userId, pointer);

            Double result = (totalIn != null ? totalIn : 0.0) - (totalOut != null ? totalOut : 0.0);

            HistoricalBalanceResponseDto.Snapshot snapshot = new HistoricalBalanceResponseDto.Snapshot();
            snapshot.setDate(pointer);
            snapshot.setAmount(BigDecimal.valueOf(result));
            snapshots.add(snapshot);

            pointer = pointer.plusDays(1); // veya saatlik: plusHours(1)
        }

        HistoricalBalanceResponseDto response = new HistoricalBalanceResponseDto();
        response.setSnapshots(snapshots);
        return response;
    }
    
   @Override
   public BalanceAtTimeResponseDto balanceAtTime(Long userId, LocalDateTime atTime) {
    Double totalIn = transactionRepository.sumToUserUntil(userId, atTime);
    Double totalOut = transactionRepository.sumFromUserUntil(userId, atTime);

    Double result = (totalIn != null ? totalIn : 0.0) - (totalOut != null ? totalOut : 0.0);

    return new BalanceAtTimeResponseDto(userId, result, atTime);
}

  
}
