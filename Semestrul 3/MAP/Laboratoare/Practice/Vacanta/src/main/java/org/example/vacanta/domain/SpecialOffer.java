package org.example.vacanta.domain;

import java.time.LocalDateTime;

public class SpecialOffer extends Entity<Double>{
    private Double hotelId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer percents;

    public SpecialOffer(Double ID, Double hotelId, LocalDateTime startDate, LocalDateTime endDate, Integer percents) {
        super(ID);
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percents = percents;
    }

    public Double getHotelId() {
        return hotelId;
    }

    public void setHotelId(Double hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getPercents() {
        return percents;
    }

    public void setPercents(Integer percents) {
        this.percents = percents;
    }
}
