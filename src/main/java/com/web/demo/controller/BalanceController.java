package com.web.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.demo.dto.Request.BalanceAtTimeRequestDto;
import com.web.demo.dto.Request.HistoricalBalanceRequestDto;
import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Response.BalanceAtTimeResponseDto;
import com.web.demo.dto.Response.BalanceResponseDto;
import com.web.demo.dto.Response.HistoricalBalanceResponseDto;
import com.web.demo.dto.Response.UserResponseDto;

import org.springframework.http.ResponseEntity;
import com.web.demo.service.abstracts.BalanceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.demo.model.User;
import com.web.demo.dto.Request.BalanceRequestDto;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    // @PostMapping("/create-balance")
    // public ResponseEntity<BalanceResponseDto> createBalance(
    //     @AuthenticationPrincipal User user, // JWT'den geliyor
    //     @RequestBody BalanceRequestDto requestDto) {

    //     String email = user.getUsername(); // Email ya da username
        
    //     // Servise email ile birlikte gönderiyoruz
    //     BalanceResponseDto responseDto = balanceService.createBalance(email, requestDto);
    //     System.out.println("Gelenler create : " + " " + responseDto.getUserId() + " " + responseDto.getBalancesCreatedAt() + " "+ responseDto.getBalancesLastUpdatedAt());

    //     return ResponseEntity.ok(responseDto);
    // }

     @PostMapping("/create-balance")
    public ResponseEntity<BalanceResponseDto> createBalance(
        @AuthenticationPrincipal User user) {

        String email = user.getUsername(); // Email ya da username
        
        // Servise email ile birlikte gönderiyoruz
        BalanceResponseDto responseDto = balanceService.createBalance(email);
        System.out.println("Gelenler create : " + " " + responseDto.getUserId() + " " + responseDto.getBalancesCreatedAt() + " "+ responseDto.getBalancesLastUpdatedAt());

        return ResponseEntity.ok(responseDto);
    }
    



    @GetMapping("/current")
    public ResponseEntity<BalanceResponseDto> getCurrentBalance(@AuthenticationPrincipal User user) {
        String email = user.getUsername(); // email burada
        System.out.println("gelennn email " + email);
        return ResponseEntity.ok(balanceService.currentBalanceByEmail(email));
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
