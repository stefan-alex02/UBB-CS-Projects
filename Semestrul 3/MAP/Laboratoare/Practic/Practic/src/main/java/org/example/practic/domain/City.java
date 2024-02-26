package org.example.practic.domain;

public class City extends Entity<String> {
    private String name;

    public City(String ID, String name) {
        super(ID);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
