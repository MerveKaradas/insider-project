package com.web.demo.strategy;

import com.web.demo.dto.Request.TransactionRequestDto;
import org.springframework.stereotype.Service;

@Service("CREDIT") // kredi verme
public class CreditStrategy implements TransactionStrategy {

    @Override
    public void processTransaction(TransactionRequestDto request) {
        // Kullanıcı bakiye al -> Amount ekle -> Veritabanına kaydet
        // Eğer kredi limiti aşılırsa hata fırlat
        // Eğer kredi limiti yeterliyse, yeni bakiye hesapla ve veritabanına kaydet
    }
    
}
