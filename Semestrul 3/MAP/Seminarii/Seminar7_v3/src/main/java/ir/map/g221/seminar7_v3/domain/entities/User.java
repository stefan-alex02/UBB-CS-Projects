package ir.map.g221.seminar7_v3.domain.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final Set<User> friends = new HashSet<>();

    public User(Long Id, String firstName, String lastName) {
        super(Id);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName) {
        super(0L);

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

    /**
     * Adds a new friend to the list of friends.
     * @param newFriend the new friend.
     * @return true if the added friend was new, false if it already existed.
     */
    public boolean addFriend(User newFriend) {
        return friends.add(newFriend);
    }

    public Set<User> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "ID : " + id + " | " +
                "First name : '" + firstName + "' | " +
                "Last name : '" + lastName + "' | " +
                "Friends list : [ " +
                friends.stream()
                        .map(user -> user.getId().toString())
                        .collect(Collectors.joining(" , ")) + " ].";
    }
}
