package com.web.demo.model;

public enum TransactionStatus {
    PENDING, // işlem beklemede
    PROCESSING, // işlem işleniyor
    SUCCESS, // işlem tamamlandı
    FAILED // işlem başarısız oldu
}