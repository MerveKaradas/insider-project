package com.web.demo.mapper;

import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.model.Balance;
import com.web.demo.model.User;


public class BalanceMapper {


    public static BalanceResponseDto toDto(Balance balance) {

        if (balance == null) return null;

        return new BalanceResponseDto(
            balance.getBalancesId(),
            balance.getBalancesUserId().getId(),
            balance.getBalancesAmount(),
            balance.getBalancesLastUpdatedAt()
        );
    }

     // DTO → Entity dönüşüm
    public static Balance toEntity(BalanceResponseDto dto, User user) {
        Balance balance = new Balance();
        balance.setBalancesUserId(user); // Entity ilişkisi nesne ile set edilir
        balance.setBalancesAmount(dto.getAmount()); // DTO alanına göre değiştir
        return balance;
    }
    
}
