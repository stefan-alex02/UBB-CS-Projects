package ir.map.g221.domain.entities.dtos;

import java.time.LocalDateTime;

import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;

public class FriendshipDetails {
    private final User targetUser;
    private final User friend;
    private final LocalDateTime friendsFromDate;

    private FriendshipDetails(User targetUser, User friend, LocalDateTime friendsFromDate) {
        this.targetUser = targetUser;
        this.friend = friend;
        this.friendsFromDate = friendsFromDate;
    }

    /**
     * Creates details for a friendship of a specified user.
     * @param friendship the friendship involved.
     * @param ofUser the target user, for whom the details are required.
     * @return the friendship details.
     * @throws IllegalArgumentException if target user does not belong to the friendship.
     */
    public static FriendshipDetails of(Friendship friendship, User ofUser) throws IllegalArgumentException {
        return new FriendshipDetails(ofUser, friendship.theOtherFriend(ofUser), friendship.getFriendsFromDate());
    }

    public LocalDateTime getFriendsFromDate() {
        return friendsFromDate;
    }

    @Override
    public String toString() {
        return friend.getLastName() + " | " +
                friend.getFirstName() + " | " +
                friendsFromDate.toString();
    }
}
