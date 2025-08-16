package com.web.demo.mapper;

import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.dto.Response.TransactionResponseDto;
import com.web.demo.model.Transaction;
import com.web.demo.model.User;

public class TransactionMapper {

    // dto -> entity
    public static Transaction toEntity(TransactionRequestDto dto, User fromUser,User toUser) {
        return new Transaction(
            fromUser, 
            toUser,  
            dto.getTransactionAmount(), 
            dto.getTransactionType(),
            null );
    }

    public static TransactionResponseDto toDto(Transaction transaction){
        return new TransactionResponseDto(
            transaction.getTransactionsId(),
            transaction.getFromUser().getId(),
            transaction.getToUser().getId(),
            transaction.getTransactionAmount(),
            transaction.getTransactionsCreatedAt(),
            transaction.getStatus(),
            transaction.getType()
        );

    }
    
}
