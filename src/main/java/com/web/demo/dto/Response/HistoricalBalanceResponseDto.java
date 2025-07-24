package com.web.demo.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class HistoricalBalanceResponseDto {



    private List<Snapshot> snapshots;

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }
    

    public static class Snapshot {
        private LocalDateTime date;
        private BigDecimal amount;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
