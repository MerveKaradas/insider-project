package com.web.demo.repository.abstracts;

import com.web.demo.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.toUserId.id = :userId AND t.transactionsCreatedAt <= :time")
    BigDecimal sumToUserUntil(@Param("userId") Long userId, @Param("time") LocalDateTime time);

    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.fromUserId.id = :userId AND t.transactionsCreatedAt <= :time")
    BigDecimal sumFromUserUntil(@Param("userId") Long userId, @Param("time") LocalDateTime time);

    List<Transaction> findAllByFromUserId_IdOrToUserId_IdOrderByTransactionsCreatedAtDesc(Long fromId, Long toId);
    //Kullanıcının gönderici ya da alıcı olduğu tüm işlemleri getirir.

}

