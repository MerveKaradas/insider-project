package com.web.demo.repository.abstracts;

import com.web.demo.model.Transaction;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

     @Query("SELECT SUM(t.transactions_amount) FROM Transactions t WHERE t.to_userId.id = :userId AND t.transactionsCreatedAt <= :time")
    Double sumToUserUntil(@Param("userId") Long userId, @Param("time") LocalDateTime time);

    @Query("SELECT SUM(t.transactions_amount) FROM Transactions t WHERE t.from_userId.id = :userId AND t.transactionsCreatedAt <= :time")
    Double sumFromUserUntil(@Param("userId") Long userId, @Param("time") LocalDateTime time);
    
}
