package com.web.demo.strategy;

import org.springframework.stereotype.Service;
import com.web.demo.dto.Request.TransactionRequestDto;
import com.web.demo.model.Transaction;

@Service("WITHDRAW") //para çekme
public class WithdrawStrategy implements TransactionStrategy {

    @Override
    public Transaction processTransaction(TransactionRequestDto request) {
        // Bakiye kontrol yap -> Çekilecek tutarı bakiyeden çıkar -> Veritabanına kaydet yoksa hata ver
        // Kullanıcı bakiye al -> Amount çıkar -> Veritabanına kaydet
        // Eğer bakiye yetersizse hata fırlat
        // Eğer bakiye yeterliyse, yeni bakiye hesapla ve veritabanına kaydet
        // Örnek:
        // BigDecimal currentBalance = userService.getBalance(request.getFromUserId());
        // if (currentBalance.compareTo(request.getTransactionAmount()) < 0) {
        //     throw new InsufficientBalanceException("Yetersiz bakiye");
        // }
        // BigDecimal newBalance = currentBalance.subtract(request.getTransactionAmount());
        // userService.updateBalance(request.getFromUserId(), newBalance);
        // Veritabanına kaydetme işlemi
        // Transaction transaction = new Transaction(request.getFromUserId(), null, request.getTransactionAmount
        // transaction.setTransactionType(TransactionType.WITHDRAW);
        // transactionRepository.save(transaction);
        // Bu kısımda, request nesnesinden gerekli bilgileri alarak
        // işlemi gerçekleştirebilirsiniz.
        // Örneğin, request.getFromUserId() ile kullanıcı ID'sini alabilir
        // ve request.getTransactionAmount() ile çekilecek tutarı alabilirsiniz.
        // Daha sonra, bu bilgileri kullanarak kullanıcı bakiyesini güncelleyebilir
        // ve gerekli veritabanı işlemlerini gerçekleştirebilirsiniz.
        // Bu işlem başarılı olursa, TransactionResponseDto oluşturabilir ve
        // işlem durumunu ayarlayabilirsiniz.
        // Örneğin:


        // Eğer işlem başarılıysa, TransactionResponseDto oluştur ve döndür
        // Eğer işlem başarısızsa, TransactionResponseDto oluştur ve FAILED durumunu ayarla
        // Örnek:
        // TransactionResponseDto response = new TransactionResponseDto();
        // response.setTransactionStatus(TransactionStatus.COMPLETED);
        // return response;
        // Not: Bu kısımda veritabanı işlemleri ve hata yönetimi
        // detaylandırılmalıdır.
        // Bu örnek, gerçek uygulama gereksinimlerine göre genişletilmelir.
        // Örneğin, bakiye kontrolü, hata fırlatma, veritabanı güncelleme gibi işlemler
        // eklenmelidir.    
        // Bu kısımda, request nesnesinden gerekli bilgileri alarak
        // işlemi gerçekleştirebilirsiniz.
    }
    
}
