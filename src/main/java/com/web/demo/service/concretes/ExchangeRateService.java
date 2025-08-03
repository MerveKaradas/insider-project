package com.web.demo.service.concretes;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
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
    private final int cacheTtl;

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


   
    public BigDecimal getExchangeRate(String from, String to) {
        String url = String.format("%s/latest?from=%s&to=%s", apiURL, from, to);

        ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

        if (response != null && response.getRates() != null && response.getRates().containsKey(to)) {
            return response.getRates().get(to);
        }

        throw new RuntimeException("Kur bilgisi alınamadı.");
    }

    // public BigDecimal getExchangeRate(String from, String to) {
    //     String key = "exchangeRate_" + from + "_" + to;
    //     String url = String.format("%s/latest?from=%s&to=%s", apiURL, from, to);

    //     try {
    //         ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

    //         if (response != null && response.getRates() != null) {
    //             BigDecimal rate = response.getRates();

    //             redisTemplate.opsForValue().set(key, rate, Duration.ofHours(cacheTtl));
    //             return rate;
    //         } else {
    //             throw new RuntimeException("Yanıt hatalı.");
    //         }

    //     } catch (Exception e) {
    //         System.err.println("Kur verisi alınamadı, cache'e bakıyoruz: " + e.getMessage());

    //         BigDecimal cachedRate = (BigDecimal) redisTemplate.opsForValue().get(key);
    //         if (cachedRate != null) {
    //             return cachedRate;
    //         } else {
    //             throw new RuntimeException("Kur alınamadı ve cache de boş: " + from + " -> " + to);
    //         }
    //     }
    // }



    // public Map<String, BigDecimal> getMultipleExchangeRates(String from, List<String> toCurrencies) {

    //     String joinedTo = String.join(",", toCurrencies);
    //     String url = String.format("%s/latest?base=%s&symbols=%s", apiURL, from, joinedTo);

    //     try {
    //         ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

    //         if (response != null && response.getRate() != null) {
    //             Map<String, BigDecimal> rates = response.getRate();

    //             // Her biri ayrı ayrı cache'e eklensin
    //             for (String to : toCurrencies) {
    //                 redisTemplate.opsForValue().set(from + "_" + to, rates.get(to), Duration.ofHours(6));
    //             }

    //             return rates;
    //         }

    //         throw new RuntimeException("Kur bilgisi alınamadı.");
    //     } catch (Exception e) {
    //         // Cache fallback
    //         Map<String, BigDecimal> fallbackRates = new HashMap<>();
    //         for (String to : toCurrencies) {
    //             BigDecimal cached = (BigDecimal) redisTemplate.opsForValue().get(from + "_" + to);
    //             if (cached != null) {
    //                 fallbackRates.put(to, cached);
    //             }
    //         }

    //         if (!fallbackRates.isEmpty()) return fallbackRates;

    //         throw new RuntimeException("Hem kur hem cache alınamadı.");
    //     }
    // }


     
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

