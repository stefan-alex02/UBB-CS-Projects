package ir.map.g221.domain;

import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.graphs.Path;

import java.util.List;
import java.util.stream.Collectors;

public class Community {
    private final List<User> users;
    private final Path<User> friendshipPath;

    public Community(List<User> users) {
        this.users = users;
        friendshipPath = null;
    }

    public Community(List<User> users, Path<User> friendshipPath) {
        this.users = users;
        this.friendshipPath = friendshipPath;
    }

    public Path<User> getFriendshipPath() {
        return friendshipPath;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    @Override
    public String toString() {
        return "Community:\n" + users.stream()
                .map(User::toString)
                .collect(Collectors.joining("\n"));
    }
}
