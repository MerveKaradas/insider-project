package com.web.demo.dto.Request;

import java.time.LocalDateTime;

public class BalanceAtTimeRequestDto {
    private LocalDateTime atTime;

    public LocalDateTime getAtTime() {
        return atTime;
    }

    public void setAtTime(LocalDateTime atTime) {
        this.atTime = atTime;
    }
}
