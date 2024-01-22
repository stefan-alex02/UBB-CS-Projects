package org.example.vacanta.domain;

public class Location extends Entity<Double>{
    private String locationName;

    public Location(Double ID, String locationName) {
        super(ID);
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
