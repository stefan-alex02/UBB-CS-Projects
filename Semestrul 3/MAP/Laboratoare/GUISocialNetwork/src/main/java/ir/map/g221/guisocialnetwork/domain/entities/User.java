package ir.map.g221.guisocialnetwork.domain.entities;

import ir.map.g221.guisocialnetwork.utils.graphs.ConnectedComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class User extends Entity<Long> {
    private final String username;
    private String firstName;
    private String lastName;
    private final String password;
    private final Set<User> friends = new HashSet<>();

    public User(Long Id, String username, String firstName, String lastName, String password) {
        super(Id);

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(String username, String firstName, String lastName, String password) {
        super(0L);

        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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
    public boolean removeFriend(User removedFriend) {
        return friends.removeIf(friend -> friend.equals(removedFriend));
    }

    /**
     * Check if the user has a specific friend.
     * @param friend The friend.
     * @return true if the two users are friends, false otherwise.
     */
    public boolean hasFriend(User friend) {
        return friends.contains(friend);
    }

    public Set<User> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "ID : " + id + " | " +
                "Username: " + username + " | " +
                "First name : '" + firstName + "' | " +
                "Last name : '" + lastName;
    }

    public String toString(ConnectedComponent<User> usersComponent) {
        return this + "Friends list : [ " +
                usersComponent.getNeighboursDataOf(this).stream()
                        .map(neighbour -> neighbour.getId().toString())
                        .collect(Collectors.joining(" , ")) +
                " ].";
    }
}
