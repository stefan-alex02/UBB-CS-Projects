package org.example.modelpractic.domain;

import java.time.LocalDateTime;

public class Order extends Entity<Long>{
    private Person person;
    private Driver taxiDriver;
    private LocalDateTime date;

    public Order(Long ID, Person person, Driver taxiDriver, LocalDateTime date) {
        super(ID);
        this.person = person;
        this.taxiDriver = taxiDriver;
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Driver getTaxiDriver() {
        return taxiDriver;
    }

    public void setTaxiDriver(Driver taxiDriver) {
        this.taxiDriver = taxiDriver;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
