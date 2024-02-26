package org.example.modelpractic.domain;

public class Person extends Entity<Long> {
    protected String username;
    protected String name;
    protected PersonType personType;

    public Person(Long ID, String username, String name) {
        super(ID);
        this.username = username;
        this.name = name;
        this.personType = PersonType.CLIENT;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
