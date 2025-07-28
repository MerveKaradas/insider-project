package com.web.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.demo.dto.Request.BalanceAtTimeRequestDto;
import com.web.demo.dto.Request.HistoricalBalanceRequestDto;
import com.web.demo.dto.Response.BalanceAtTimeResponseDto;
import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.dto.Response.HistoricalBalanceResponseDto;

import org.springframework.http.ResponseEntity;
import com.web.demo.service.abstracts.BalanceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.web.demo.model.User;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/current")
    public ResponseEntity<BalanceResponseDto> getCurrentBalance(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(balanceService.currentBalanceByUsername(username));
    }   

   @GetMapping("/at-time")
    public ResponseEntity<BalanceAtTimeResponseDto> getBalanceAtTime(
            @AuthenticationPrincipal String username,
            @RequestBody BalanceAtTimeRequestDto request) {

        Long userId = balanceService.getUserIdByUsername(username);
        BalanceAtTimeResponseDto response = balanceService.balanceAtTime(userId, request.getAtTime());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/historical") // POST daha mantıklı çünkü body alıyoruz
    public ResponseEntity<HistoricalBalanceResponseDto> getHistoricalBalance(
            @AuthenticationPrincipal User user,
            @RequestBody HistoricalBalanceRequestDto request) {

        HistoricalBalanceResponseDto response = balanceService.historicalBalance(user.getId(), request.getStart(), request.getEnd());
        return ResponseEntity.ok(response);
    }




    
}
