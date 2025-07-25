package com.web.demo.model;

public enum TransactionType {
    DEBIT, // borç işlemi
    REFUND, // iade
    PAYMENT, // ödeme
    TRANSFER,      // Kullanıcıdan kullanıcıya para transferi
    DEPOSIT,       // Sistemden kullanıcıya (para yatırma)
    WITHDRAWAL,    // Kullanıcıdan sisteme (para çekme)
    CREDIT,        // Sistemden kullanıcıya borç/kredi
    DEBT_PAYMENT   // Kullanıcıdan sisteme kredi geri ödemesi
}