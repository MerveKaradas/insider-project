package com.web.demo.repository.abstracts;

import com.web.demo.model.Balance;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByBalancesUserId_Id(Long userId);

    void save(BigDecimal newBalance);

   
    
    
}
