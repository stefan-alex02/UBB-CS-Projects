package ir.map.g221.domain;

import ir.map.g221.domain.entities.User;

import java.util.List;

public class Community {
    private final List<User> users;

    public Community(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public String toString() {
        return "Community:" + users;
    }
}
