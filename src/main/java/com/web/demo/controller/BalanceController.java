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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.web.demo.service.abstracts.BalanceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.demo.model.User;


@RestController
@RequestMapping("/api/v1/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping("/create-balance")
    public ResponseEntity<BalanceResponseDto> createBalance(
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = ((User) userDetails).getEmail();
        BalanceResponseDto responseDto = balanceService.createBalance(email);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/current")
    public ResponseEntity<BalanceResponseDto> getCurrentBalance(
        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = ((User) userDetails).getEmail();
        return ResponseEntity.ok(balanceService.currentBalanceByEmail(email));
    }

    @PostMapping("/at-time")
    public ResponseEntity<BalanceAtTimeResponseDto> getBalanceAtTime(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BalanceAtTimeRequestDto request) {

         if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = ((User) userDetails).getEmail();
        BalanceAtTimeResponseDto response = balanceService.balanceAtTime(email, request.getAtTime());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/historical") // POST daha mantıklı çünkü body alıyoruz
    public ResponseEntity<HistoricalBalanceResponseDto> getHistoricalBalance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody HistoricalBalanceRequestDto request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = ((User) userDetails).getEmail();
        HistoricalBalanceResponseDto response = balanceService.historicalBalance(email, request.getStart(), request.getEnd());
        return ResponseEntity.ok(response);
    }




    
}
