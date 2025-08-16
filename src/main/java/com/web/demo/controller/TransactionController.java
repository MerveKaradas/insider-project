package com.web.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.web.demo.service.abstracts.TransactionService;
import com.web.demo.dto.Request.DepositRequestDto;
import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.dto.Request.WithdrawRequestDto;
import com.web.demo.dto.Response.TransactionResponseDto;
import com.web.demo.mapper.TransactionMapper;
import com.web.demo.model.Transaction;
import com.web.demo.model.TransactionType;
import com.web.demo.model.User;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionResponseDto> creditTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody TransactionRequestDto request) {

        Transaction transaction = transactionService.executeTransaction(request, user.getEmail());

        return ResponseEntity.ok(TransactionMapper.toDto(transaction));
    } 

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> depositTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody DepositRequestDto request) {

        TransactionRequestDto dto = new TransactionRequestDto(
            null, // fromUser = system
            user.getId(),
            request.getTransactionAmount(),
            TransactionType.DEPOSIT
        );

        Transaction transaction = transactionService.executeTransaction(dto, user.getEmail());

        return ResponseEntity.ok(TransactionMapper.toDto(transaction));
    } 

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdrawTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody WithdrawRequestDto request) {

         TransactionRequestDto dto = new TransactionRequestDto(
            user.getId(), 
            null, 
            request.getTransactionAmount(),
            TransactionType.WITHDRAWAL
        );

        Transaction transaction = transactionService.executeTransaction(dto, user.getEmail());

        return ResponseEntity.ok(TransactionMapper.toDto(transaction));
    } 

    @PostMapping("/debit")
    public ResponseEntity<TransactionResponseDto> debitTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody TransactionRequestDto request) {

        Transaction transaction = transactionService.executeTransaction(request, user.getEmail());

        return ResponseEntity.ok(TransactionMapper.toDto(transaction));
    } 

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transferTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody TransactionRequestDto request) {

        Transaction transaction = transactionService.executeTransaction(request, user.getEmail());

        return ResponseEntity.ok(TransactionMapper.toDto(transaction));
    } 


    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponseDto>> transactionHistory(
            @AuthenticationPrincipal User user) {

        List<Transaction> transactions = transactionService.getHistory(user.getEmail());
        List<TransactionResponseDto> dtoList = transactions.stream()
            .map(TransactionMapper::toDto)
            .toList();

        return ResponseEntity.ok(dtoList);
    } 
    
 
    @GetMapping("history/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
        @AuthenticationPrincipal User user,
        @PathVariable Long id) {

        Transaction transaction = transactionService.findByIdIfBelongsToUser(id, user); // Sadece o kullanıcıya aitse göster
        return ResponseEntity.ok(TransactionMapper.toDto(transaction));
    }


}
