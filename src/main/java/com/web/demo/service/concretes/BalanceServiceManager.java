package com.web.demo.service.concretes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.web.demo.service.abstracts.BalanceService;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.dto.Request.BalanceRequestDto;
import com.web.demo.dto.Response.BalanceAtTimeResponseDto;
import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.dto.Response.HistoricalBalanceResponseDto;
import com.web.demo.mapper.BalanceMapper;
import com.web.demo.model.Balance;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.repository.abstracts.UserRepository;

import java.util.List;

@Service
public class BalanceServiceManager implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;


    public BalanceServiceManager(BalanceRepository balanceRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
      
    }
    public Long getUserIdByUsername(String username){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanici bulunamadi"));

            

        return user.getId();
    }

    public BalanceResponseDto currentBalanceByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanıcı bulunamadı"));

        Balance balance = balanceRepository.findByBalancesUserId_Id(user.getId())
            .orElseThrow(() -> new RuntimeException("Kullanıcının cüzdanı bulunamadı"));

        BalanceResponseDto dto = new BalanceResponseDto();
        dto.setUserId(user.getId());
        dto.setAmount(balance.getBalancesAmount());
        dto.setBalancesLastUpdatedAt(balance.getBalancesLastUpdatedAt());

        return dto;
    }

    public BalanceResponseDto currentBalanceByUsername(String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanici bulunamadi"));

        Balance balance = balanceRepository.findByBalancesUserId_Id(user.getId())
        .orElseThrow(() -> new RuntimeException("Kullanıcının cüzdanı bulunamadı"));

        BalanceResponseDto dto = new BalanceResponseDto();
        dto.setUserId(user.getId());
        dto.setAmount(balance.getBalancesAmount());
        dto.setBalancesLastUpdatedAt(balance.getBalancesLastUpdatedAt());

        return dto;
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
            BigDecimal totalIn = transactionRepository.sumToUserUntil(userId, pointer);
            BigDecimal totalOut = transactionRepository.sumFromUserUntil(userId, pointer);

            totalIn = (totalIn != null) ? totalIn : BigDecimal.ZERO;
            totalOut = (totalOut != null) ? totalOut : BigDecimal.ZERO;

            BigDecimal result = totalIn.subtract(totalOut);

            HistoricalBalanceResponseDto.Snapshot snapshot = new HistoricalBalanceResponseDto.Snapshot();
            snapshot.setDate(pointer);
            snapshot.setAmount(result);
            snapshots.add(snapshot);

            pointer = pointer.plusDays(1); // veya saatlik: plusHours(1)
        }

        HistoricalBalanceResponseDto response = new HistoricalBalanceResponseDto();
        response.setSnapshots(snapshots);
        return response;
    }
    
   @Override
   public BalanceAtTimeResponseDto balanceAtTime(Long userId, LocalDateTime atTime) {
    BigDecimal totalIn = transactionRepository.sumToUserUntil(userId, atTime);
    BigDecimal totalOut = transactionRepository.sumFromUserUntil(userId, atTime);

    totalIn = (totalIn != null) ? totalIn : BigDecimal.ZERO;
    totalOut = (totalOut != null) ? totalOut : BigDecimal.ZERO;

    BigDecimal result = totalIn.subtract(totalOut);

    return new BalanceAtTimeResponseDto(userId, result, atTime);
}

//    @Override
//    public BalanceResponseDto createBalance(String email, BalanceRequestDto requestDto) {
//         User user = userRepository.findByEmail(email)
//             .orElseThrow(() -> new UsernameNotFoundException("Balance olusturmak icin kayıtlı kullanici bulunamadi"));

//         BalanceResponseDto dto = new BalanceResponseDto();
//         dto.setUserId(requestDto.getUserId());
//         dto.setAmount(requestDto.getAmount());
//         dto.setBalancesCreatedAt(LocalDateTime.now());
//         dto.setBalancesLastUpdatedAt(LocalDateTime.now());
//         balanceRepository.save(BalanceMapper.toEntity(dto, user));
        
//         System.out.println("Balance olustuuuldu. kullanici mail : " + user.getEmail() + dto.getUserId() + " " + dto.getAmount() + " " + dto.getBalancesCreatedAt() + " " + dto.getBalancesLastUpdatedAt() );
//         return dto;
//     }


    @Override
    public BalanceResponseDto createBalance(String email) {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Balance olusturmak icin kayıtlı kullanici bulunamadi"));

            BalanceResponseDto dto = new BalanceResponseDto();
            dto.setUserId(user.getId());
            dto.setAmount(BigDecimal.ZERO);
            dto.setBalancesCreatedAt(LocalDateTime.now());
            dto.setBalancesLastUpdatedAt(LocalDateTime.now());
            
            balanceRepository.save(BalanceMapper.toEntity(dto, user));
            
            System.out.println("Balance olustuuuldu. kullanici mail : " + user.getEmail() + dto.getUserId() + " " + dto.getAmount() + " " + dto.getBalancesCreatedAt() + " " + dto.getBalancesLastUpdatedAt() );
            return dto;
        }



  
}
