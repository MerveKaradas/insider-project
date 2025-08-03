package com.web.demo.service.concretes;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.web.demo.dto.Response.ExchangeRateResponseDto;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate; //HTTP istekleri gönderir
    private final String apiURL;
    private final RedisTemplate<String, Object> redisTemplate; // redis ile cachelemek
    private final int cacheTtl; // cache'de verinin kalma süresi (application.yml dosyasından geliyor)

    public ExchangeRateService(RestTemplate restTemplate,
                               RedisTemplate<String, Object> redisTemplate,
                               @Value("${exchange.api.url}") String apiURL,
                               @Value("${exchange.rate.cache.ttl.hours}") int cacheTtl
                               ) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.apiURL = apiURL;
        this.cacheTtl = cacheTtl;
    }


    public  Map<String,BigDecimal> getExchangeRate(String from, String to) {
        String key = "exchangeRate_" + from + "_" + to;
        String url = String.format("%s/latest?from=%s&to=%s", apiURL, from, to);

        try {
            ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

            if (response != null && response.getRates() != null) {
                 Map<String,BigDecimal> rate = response.getRates();

                redisTemplate.opsForValue().set(key, rate, Duration.ofHours(cacheTtl));
                return rate;
            } else {
                throw new RuntimeException("Yanıt hatalı.");
            }

        } catch (Exception e) {
            System.err.println("Kur verisi alınamadı, cache'e bakıyoruz: " + e.getMessage());

             Map<String,BigDecimal> cachedRate = ( Map<String,BigDecimal>) redisTemplate.opsForValue().get(key);
            if (cachedRate != null) {
                return cachedRate;
            } else {
                throw new RuntimeException("Kur alınamadı ve cache de boş: " + from + " -> " + to);
            }
        }
    }

    public Map<String, BigDecimal> getAllExchangeRates(String baseCurrency) {
        String url = String.format("%s/latest?base=%s", apiURL, baseCurrency);
        String key = "exchangeRate_" + baseCurrency + "_ALL";

        try {
            ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

            if (response != null && response.getRates() != null) {
                Map<String, BigDecimal> rates = response.getRates();
                redisTemplate.opsForValue().set(key, rates, Duration.ofHours(cacheTtl));
                return rates;
            } else {
                throw new RuntimeException("Yanıt hatalı.");
            }

        } catch (Exception e) {
            System.err.println("API başarısız, cache'ten çekiliyor: " + e.getMessage());
            Map<String, BigDecimal> cached = ( Map<String,BigDecimal>)redisTemplate.opsForValue().get(key);
            if (cached != null) {
                return cached;
            } else {
                throw new RuntimeException("Kur alınamadı ve cache de boş.");
            }
        }
    }


     
    // Cache her gün 01:00'da temizlenecek
    @Scheduled(cron = "0 0 1 * * *")
    public void evictCache() {

        Set<String> keys = redisTemplate.keys("exchangeRate_*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        System.out.println("Cache temizlendi.");
        
    }

    
}

