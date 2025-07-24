package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import org.springframework.stereotype.Service;

@Service("TRANSFER") // Transfer işlemi
public class TransferStrategy implements TransactionStrategy {

    @Override
    public void processTransaction(TransactionRequestDto request) {
        // Transfer işlemi için gerekli adımlar:
        // 1. Gönderen kullanıcının bakiyesini kontrol et
        // 2. Alıcı kullanıcının bakiyesini güncelle
        // 3. Gönderen kullanıcının bakiyesinden tutarı düş
        // 4. Veritabanına kaydet
    }
    
}
