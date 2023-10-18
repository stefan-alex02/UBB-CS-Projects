package ir.map.g221.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final Set<User> friends = new HashSet<>();

    public User(Long Id, String firstName, String lastName) {
        super(Id);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void addFriend(User newFriend) {
        friends.add(newFriend);
    }

    public boolean removeFriendById(Long id) {
        return friends.removeIf(fr -> Objects.equals(fr.getId(), id));
    }

    public Set<User> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "ID : " + id + " | " +
                "First name : '" + firstName + "' | " +
                "Last name : '" + lastName + "' | " +
                "Friends list : " + friends;
    }
}
