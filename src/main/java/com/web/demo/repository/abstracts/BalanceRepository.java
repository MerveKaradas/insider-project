package com.web.demo.repository.abstracts;

import com.web.demo.model.Balance;

import java.math.BigDecimal;
import java.util.Optional;
import com.web.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BalanceRepository extends JpaRepository<Balance, Long> {

    void updateBalance(Long toUserId, BigDecimal newBalance);

    //Optional<User> findByUserId(Long toUserId);

    Optional<Balance> findBalanceByUserId(Long userId);

    void save(BigDecimal newBalance);

   
    
    
}
