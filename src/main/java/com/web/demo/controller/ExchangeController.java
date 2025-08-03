package com.web.demo.controller;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.web.demo.service.concretes.ExchangeRateService;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<?> getRate(@RequestParam String from, @RequestParam String to) {
         Map<String,BigDecimal> rate = exchangeRateService.getExchangeRate(from, to);
        return ResponseEntity.ok(Map.of("rate", rate, "from", from, "to", to));
    }
}