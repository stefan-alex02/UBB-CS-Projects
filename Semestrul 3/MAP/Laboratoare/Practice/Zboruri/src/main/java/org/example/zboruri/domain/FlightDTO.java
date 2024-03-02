package org.example.zboruri.domain;

import java.time.LocalDateTime;

public class FlightDTO extends Entity<Long>{
    private String from;
    private String to;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private Integer seats;
    private Integer remainingSeats;

    private FlightDTO(Long ID, String from, String s, LocalDateTime departureTime, LocalDateTime landingTime, Integer seats, Integer remainingSeats) {
        super(ID);
        this.from = from;
        to = s;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.seats = seats;
        this.remainingSeats = remainingSeats;
    }

    public static FlightDTO ofFlight(Flight flight, int occupiedSeats) {
        return new FlightDTO(flight.getID(),
                flight.getFrom(),
                flight.getTo(),
                flight.getDepartureTime(),
                flight.getLandingTime(),
                flight.getSeats(),
                flight.getSeats() - occupiedSeats);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
    }
}
