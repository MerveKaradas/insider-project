package com.web.demo.service.abstracts;

import java.time.LocalDateTime;

import com.web.demo.dto.Request.BalanceRequestDto;
import com.web.demo.dto.Response.BalanceAtTimeResponseDto;
import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.dto.Response.HistoricalBalanceResponseDto;

public interface BalanceService {

    BalanceResponseDto currentBalance(Long userId);
    BalanceAtTimeResponseDto balanceAtTime(Long userId, LocalDateTime atTime);
    HistoricalBalanceResponseDto historicalBalance(Long userId, LocalDateTime start, LocalDateTime end);
    BalanceResponseDto currentBalanceByUsername(String username);
    Long getUserIdByUsername(String username);
    BalanceResponseDto currentBalanceByEmail(String email);
   // BalanceResponseDto createBalance(String email, BalanceRequestDto requestDto);
    BalanceResponseDto createBalance(String email);
 
}
