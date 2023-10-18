package ir.map.g221.domain;

import java.util.ArrayList;
import java.util.List;

public class Community {
    private final List<User> users;

    public Community() {
        users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
