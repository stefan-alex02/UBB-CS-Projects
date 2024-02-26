package org.example.modelpractic.domain;

public class PlacedOrder {
    private Person person;
    private Driver driver;
    private String location;
    private Integer timpAsteptare;

    public PlacedOrder(Person person, Driver driver, String location, Integer timpAsteptare) {
        this.person = person;
        this.driver = driver;
        this.location = location;
        this.timpAsteptare = timpAsteptare;
    }

    public String getLocation() {
        return location;
    }

    public Person getPerson() {
        return person;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Integer getTimpAsteptare() {
        return timpAsteptare;
    }

    public void setTimpAsteptare(Integer timpAsteptare) {
        this.timpAsteptare = timpAsteptare;
    }


}
