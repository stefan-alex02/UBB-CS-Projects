package org.example.vacanta.domain;

public class Hotel extends Entity<Double>{
    private Double locationId;
    private String hotelName;
    private Integer noRooms;
    private Double pericePerNight;
    private HotelType hotelType;

    public Hotel(Double ID, Double locationId, String hotelName, Integer noRooms, Double pericePerNight, HotelType hotelType) {
        super(ID);
        this.locationId = locationId;
        this.hotelName = hotelName;
        this.noRooms = noRooms;
        this.pericePerNight = pericePerNight;
        this.hotelType = hotelType;
    }

    public Double getLocationId() {
        return locationId;
    }

    public void setLocationId(Double locationId) {
        this.locationId = locationId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Integer getNoRooms() {
        return noRooms;
    }

    public void setNoRooms(Integer noRooms) {
        this.noRooms = noRooms;
    }

    public Double getPericePerNight() {
        return pericePerNight;
    }

    public void setPericePerNight(Double pericePerNight) {
        this.pericePerNight = pericePerNight;
    }

    public HotelType getHotelType() {
        return hotelType;
    }

    public void setHotelType(HotelType hotelType) {
        this.hotelType = hotelType;
    }
}
