package com.web.demo.repository.abstracts;

import com.web.demo.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.toUser.id = :userId AND t.transactionsCreatedAt <= :time")
    BigDecimal sumToUserUntil(@Param("userId") Long userId, @Param("time") LocalDateTime time);

    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.fromUser.id = :userId AND t.transactionsCreatedAt <= :time")
    BigDecimal sumFromUserUntil(@Param("userId") Long userId, @Param("time") LocalDateTime time);

    List<Transaction> findAllByFromUser_IdOrToUser_IdOrderByTransactionsCreatedAtDesc(Long fromId, Long toId);
    //Kullanıcının gönderici ya da alıcı olduğu tüm işlemleri getirir.

    @Query("SELECT t FROM Transaction t WHERE (t.fromUser.id = :userId OR t.toUser.id = :userId) AND t.transactionsCreatedAt BETWEEN :start AND :end ORDER BY t.transactionsCreatedAt ASC")
        List<Transaction> findAllByUserAndCreatedAtBetween(
        @Param("userId") Long userId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );


}

