package org.example.practic.domain;

import java.time.LocalDate;

public class Ticket extends Entity<Long>{
    private String trainId;
    private String departureCityId;
    private LocalDate date;

    public Ticket(Long ID, String trainId, String departureCityId, LocalDate date) {
        super(ID);
        this.trainId = trainId;
        this.departureCityId = departureCityId;
        this.date = date;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getDepartureCityId() {
        return departureCityId;
    }

    public void setDepartureCityId(String departureCityId) {
        this.departureCityId = departureCityId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
