package com.web.demo.service.concretes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.service.abstracts.BalanceService;
import com.web.demo.util.GlobalContext;
import com.web.demo.repository.abstracts.BalanceRepository;
import com.web.demo.dto.Response.BalanceAtTimeResponseDto;
import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.dto.Response.HistoricalBalanceResponseDto;
import com.web.demo.event.AuditEventPublisher;
import com.web.demo.mapper.BalanceMapper;
import com.web.demo.model.Balance;
import com.web.demo.model.Transaction;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.TransactionRepository;
import com.web.demo.repository.abstracts.UserRepository;
import java.util.List;

@Service
public class BalanceServiceManager implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AuditEventPublisher auditEventPublisher;

    public BalanceServiceManager(BalanceRepository balanceRepository, TransactionRepository transactionRepository, UserRepository userRepository, AuditEventPublisher auditEventPublisher) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.auditEventPublisher = auditEventPublisher;
      
    }

    public void logger(Balance balance,String action, String details){
        // Audit loglama
        auditEventPublisher.publish(
            "BALANCE", 
            balance.getBalancesId(), 
            action, 
            details,
            GlobalContext.getCurrentUsername(),
            GlobalContext.getIpAddress(),
            GlobalContext.getUserAgent());
    }

    public Long getUserIdByUsername(String username){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanici bulunamadi"));

        return user.getId();
    }

    @Transactional
    public BalanceResponseDto currentBalanceByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanıcı bulunamadı"));

        Balance balance = balanceRepository.findByBalancesUserId_Id(user.getId())
            .orElseThrow(() -> new RuntimeException("Kullanıcının cüzdanı bulunamadı"));

            logger(balance,"VIEW","Mevcut balance goruntulendi");
        return BalanceMapper.toDto(balance);
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
    @Transactional
    public BalanceResponseDto currentBalance(Long userId) {
       
        Balance balance = balanceRepository.findByBalancesUserId_Id(userId)
            .orElseThrow(() -> new RuntimeException( userId + "idli kullanıcının kayıtlı cüzdanı bulunamadı " ));
        
        logger(balance,"VIEW","Mevcut balance goruntulendi");
        return BalanceMapper.toDto(balance);
    }


    @Override
    @Transactional
    public HistoricalBalanceResponseDto historicalBalance(String email, LocalDateTime start, LocalDateTime end) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanıcı bulunamadı"));

        List<HistoricalBalanceResponseDto.Snapshot> snapshots = new ArrayList<>();

        // İlgili zaman aralığındaki işlemler
        List<Transaction> transactions = transactionRepository.findAllByUserIdAndCreatedAtBetween(user.getId(), start, end);

        for (Transaction transaction : transactions) {
            LocalDateTime time = transaction.getTransactionsCreatedAt(); 

            // İşlem zamanına kadar olan giriş ve çıkış toplamları
            BigDecimal totalIn = transactionRepository.sumToUserUntil(user.getId(), time);
            BigDecimal totalOut = transactionRepository.sumFromUserUntil(user.getId(), time);

            BigDecimal safeIncome = totalIn != null ? totalIn : BigDecimal.ZERO;
            BigDecimal safeExpense = totalOut != null ? totalOut : BigDecimal.ZERO;

            BigDecimal result = safeIncome.subtract(safeExpense);

            HistoricalBalanceResponseDto.Snapshot snapshot = new HistoricalBalanceResponseDto.Snapshot();
            snapshot.setDate(time); 
            snapshot.setAmount(result);
            snapshots.add(snapshot);
        }

        HistoricalBalanceResponseDto response = new HistoricalBalanceResponseDto();
        response.setSnapshots(snapshots);

        // Audit loglama
        auditEventPublisher.publish(
            "USER", 
            user.getId(), 
            "VIEW", 
            "Kullanıcı geçmiş balance bilgisini goruntuledi",
            GlobalContext.getCurrentUsername(),
            GlobalContext.getIpAddress(),
            GlobalContext.getUserAgent());

        return response;
    }


    
   @Override
   @Transactional
   public BalanceAtTimeResponseDto balanceAtTime(String email, LocalDateTime atTime) {

    User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Kullanici bulunamdi"));

    BigDecimal totalIn = transactionRepository.sumToUserUntil(user.getId(), atTime);
    BigDecimal totalOut = transactionRepository.sumFromUserUntil(user.getId(), atTime);

    totalIn = (totalIn != null) ? totalIn : BigDecimal.ZERO;
    totalOut = (totalOut != null) ? totalOut : BigDecimal.ZERO;

    BigDecimal result = totalIn.subtract(totalOut);

    // Audit loglama
        auditEventPublisher.publish(
            "USER", 
            user.getId(), 
            "VIEW", 
            "Kullanıcı " + atTime + " zamanına ait balance bilgisini goruntuledi",
            GlobalContext.getCurrentUsername(),
            GlobalContext.getIpAddress(),
            GlobalContext.getUserAgent());

    return new BalanceAtTimeResponseDto(user.getId(), result, atTime);
}


    @Override
    @Transactional
    public BalanceResponseDto createBalance(String email) {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Balance olusturmak icin kayıtlı kullanici bulunamadi"));

            Balance balance = new Balance();
            balance.setBalancesUserId(user);
            balance.setBalancesAmount(BigDecimal.ZERO);
            balance.setBalancesCreatedAt(LocalDateTime.now());
            balance.setBalancesLastUpdatedAt(LocalDateTime.now());
            
            balanceRepository.save(balance);
            logger(balance,"CREATE","Balance olusturuldu");
            
            return BalanceMapper.toDto(balance);
    }
  
}
