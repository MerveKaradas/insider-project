package com.web.demo.strategy;

import org.springframework.stereotype.Service;
import com.web.demo.dto.Request.TransactionRequestDto;

@Service("DEBT") // borç işlemi
public class DebtStrategy implements TransactionStrategy {

    @Override
    public void processTransaction(TransactionRequestDto request) {
        // Kullanıcı bakiye al -> Borç tutarını bakiyeden çıkar -> Veritabanına kaydet
        // Eğer bakiye yetersizse hata fırlat
        // Eğer bakiye yeterliyse, yeni bakiye hesapla ve veritabanına kaydet
    }
    
}
