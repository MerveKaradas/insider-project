package com.web.demo.dto.Request;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BalanceAtTimeRequestDto implements Serializable {
    
    private static final long serialVersionUID = 1001L;

    private LocalDateTime atTime;

    public LocalDateTime getAtTime() {
        return atTime;
    }

    public void setAtTime(LocalDateTime atTime) {
        this.atTime = atTime;
    }
}
