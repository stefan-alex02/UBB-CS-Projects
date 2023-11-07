package ir.map.g221.domain.entities.dtos;

import java.time.LocalDateTime;

import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;

public class FriendshipDTO {
    private final User friend;
    private final LocalDateTime friendsFromDate;

    private FriendshipDTO(User friend, LocalDateTime friendsFromDate) {
        this.friend = friend;
        this.friendsFromDate = friendsFromDate;
    }

    public static FriendshipDTO of(Friendship friendship, User ofUser) {
        return new FriendshipDTO(friendship.theOtherFriend(ofUser), friendship.getFriendsFromDate());
    }

    @Override
    public String toString() {
        return friend.getLastName() + " | " +
                friend.getFirstName() + " | " +
                friendsFromDate.toString();
    }
}
