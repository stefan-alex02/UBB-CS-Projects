package org.example.zboruri.domain;

public class Client extends Entity<String> {
    protected String name;

    public Client(String username, String name) {
        super(username);
        this.name = name;
    }

    public String getUsername() {
        return ID;
    }

    public void setUsername(String username) {
        this.ID = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
