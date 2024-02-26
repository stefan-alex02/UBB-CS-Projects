package org.example.modelpractic.domain;

public class Driver extends Person{
    private String indicativMasina;

    public Driver(Long ID, String username, String name, String indicativMasina) {
        super(ID, username, name);
        this.indicativMasina = indicativMasina;
        this.personType = PersonType.DRIVER;
    }

    public String getIndicativMasina() {
        return indicativMasina;
    }

    public void setIndicativMasina(String indicativMasina) {
        this.indicativMasina = indicativMasina;
    }
}
