package org.example.zboruri.domain;

import javafx.util.Pair;

import java.time.LocalDateTime;

public class Ticket extends Entity<Pair<String, Long>> {
    private LocalDateTime purchaseTime;

    public Ticket(Pair<String, Long> ID, LocalDateTime purchaseTime) {
        super(ID);
        this.purchaseTime = purchaseTime;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}
