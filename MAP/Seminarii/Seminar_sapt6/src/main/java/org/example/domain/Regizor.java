package org.example.domain;

public class Regizor extends Entity<Long> {
    private String nume;

    public Regizor(String nume) {
        this.nume = nume;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Regizor{" +
                "nume='" + nume + '\'' +
                ", id=" + id +
                '}';
    }
}
