package ir.map.g221.domain.entities;

import ir.map.g221.domain.generaltypes.UnorderedPair;

import java.time.LocalDateTime;

public class Friendship extends Entity<UnorderedPair<Long, Long>> {
    private final User firstUser;
    private final User secondUser;
    private final LocalDateTime friendsFromDate;

    public Friendship(User user1, User user2, LocalDateTime friendsFromDate) {
        super(UnorderedPair.ofAscending(user1.getId(), user2.getId()));
        if (user1.getId().compareTo(user2.getId()) <= 0) {
            this.firstUser = user1;
            this.secondUser = user2;
        }
        else {
            this.firstUser = user2;
            this.secondUser = user1;
        }
        this.friendsFromDate = friendsFromDate;
    }

    public Friendship(User user1, User user2) {
        this(user1, user2, LocalDateTime.now());
    }

    public LocalDateTime getFriendsFromDate() {
        return friendsFromDate;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public Boolean hasUser(User user) {
        return firstUser.equals(user) || secondUser.equals(user);
    }

    public User theOtherFriend(User user) throws IllegalArgumentException {
        if (!hasUser(user)) {
            throw new IllegalArgumentException("Specified user does not belong to object friendship.");
        }
        return firstUser.equals(user) ? secondUser : firstUser;
    }
}
