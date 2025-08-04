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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

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


    @CircuitBreaker(name = "myService", fallbackMethod = "fallbackMethod")
    @Retry(name = "myRetryService", fallbackMethod = "fallbackRetry")
    @RateLimiter(name = "myRateLimitedService")
    public Map<String, BigDecimal> getExchangeRate(String from, String to) {
        String key = "exchangeRate_" + from + "_" + to;
        String url = String.format("%s/latest?from=%s&to=%s", apiURL, from, to);

        ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

        if (response != null && response.getRates() != null) {
            Map<String, BigDecimal> rate = response.getRates();
            redisTemplate.opsForValue().set(key, rate, Duration.ofHours(cacheTtl));
            return rate;
        } else {
            throw new RuntimeException("Yanıt hatalı.");
        }
    }


    @CircuitBreaker(name = "myService", fallbackMethod = "fallbackAll")
    @Retry(name = "myRetryService", fallbackMethod = "fallbackRetryAll")
    @RateLimiter(name = "myRateLimitedService")
    public Map<String, BigDecimal> getAllExchangeRates(String baseCurrency) {
        String url = String.format("%s/latest?base=%s", apiURL, baseCurrency);
        String key = "exchangeRate_" + baseCurrency + "_ALL";

        ExchangeRateResponseDto response = restTemplate.getForObject(url, ExchangeRateResponseDto.class);

        if (response != null && response.getRates() != null) {
            Map<String, BigDecimal> rates = response.getRates();
            redisTemplate.opsForValue().set(key, rates, Duration.ofHours(cacheTtl));
            return rates;
        } else {
            throw new RuntimeException("Yanıt hatalı.");
        }
    }


    public Map<String, BigDecimal> fallbackMethod(String from, String to, Throwable t) {
        System.err.println("Fallback devrede: " + t.getMessage());

        String cacheKey = "exchangeRate_" + from + "_" + to;
        Map<String, BigDecimal> cachedRates = (Map<String, BigDecimal>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedRates != null) {
            return cachedRates;
        }

        // Cache yoksa boş map döner
        return Map.of();
    }


    public Map<String, BigDecimal> fallbackRetry(String from, String to, Throwable t) {
        System.err.println("Retry fallback devrede: " + t.getMessage());

        // Cache'e bakılır
        String cacheKey = "exchangeRate_" + from + "_" + to;
        Map<String, BigDecimal> cachedRates = (Map<String, BigDecimal>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedRates != null) {
            return cachedRates;
        }

        return Map.of();
    }


    public Map<String, BigDecimal> fallbackAll(String baseCurrency, Throwable t) {
        System.err.println("FallbackAll devrede: " + t.getMessage());

        String cacheKey = "exchangeRate_" + baseCurrency + "_ALL";
        Map<String, BigDecimal> cachedRates = (Map<String, BigDecimal>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedRates != null) {
            return cachedRates;
        }

        return Map.of();
    }


    public Map<String, BigDecimal> fallbackRetryAll(String baseCurrency, Throwable t) {
        System.err.println("Retry fallbackAll devrede: " + t.getMessage());

        String cacheKey = "exchangeRate_" + baseCurrency + "_ALL";
        Map<String, BigDecimal> cachedRates = (Map<String, BigDecimal>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedRates != null) {
            return cachedRates;
        }

        return Map.of();
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

