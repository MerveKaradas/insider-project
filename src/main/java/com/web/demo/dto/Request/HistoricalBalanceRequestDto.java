package com.web.demo.dto.Request;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistoricalBalanceRequestDto implements Serializable {

    private static final long serialVersionUID = 1002L;

    private LocalDateTime start;
    private LocalDateTime end;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
