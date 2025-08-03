package com.web.demo.dto.Response;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

    
public class ExchangeRateResponseDto implements Serializable{

    private static final long serialVersionUID = 2006L;

    private String from;
    private String to;
    private Map<String, BigDecimal> rates;

    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String base) {
        this.from = base;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String date) {
        this.to = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
    
    
