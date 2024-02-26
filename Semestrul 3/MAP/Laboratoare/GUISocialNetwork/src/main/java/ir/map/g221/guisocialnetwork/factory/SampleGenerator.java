package ir.map.g221.guisocialnetwork.factory;

import ir.map.g221.guisocialnetwork.business.FriendshipService;
import ir.map.g221.guisocialnetwork.business.UserService;
import ir.map.g221.guisocialnetwork.exceptions.SampleGeneratedException;

import java.time.LocalDateTime;

public class SampleGenerator {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private boolean alreadyGenerated = false;

    public SampleGenerator(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    public void generateSample() {
        if (alreadyGenerated) {
            throw new SampleGeneratedException("Samples have already been generated.");
        }
        alreadyGenerated = true;

        userService.addUser("username1", "firstName1", "lastName1", "password1");
        userService.addUser("username2", "firstName2", "lastName2", "password2");
        userService.addUser("username3", "firstName3", "lastName3", "password3");
        userService.addUser("username4", "firstName4", "lastName4", "password4");
        userService.addUser("username5", "firstName5", "lastName5", "password5");
        userService.addUser("username6", "firstName6", "lastName6", "password6");
        userService.addUser("username7", "firstName7", "lastName7", "password7");
        userService.addUser("username8", "firstName8", "lastName8", "password8");
        userService.addUser("username9", "firstName9", "lastName9", "password9");
        userService.addUser("username10", "firstName10", "lastName10", "password10");
        userService.addUser("username11", "firstName11", "lastName11", "password11");

        friendshipService.addFriendship(2L, 1L,
                LocalDateTime.of(2012, 12, 21, 22, 30));
        friendshipService.addFriendshipNow(1L, 3L);
        friendshipService.addFriendshipNow(3L, 4L);
        friendshipService.addFriendshipNow(2L, 4L);

        friendshipService.addFriendshipNow(5L, 6L);
        friendshipService.addFriendshipNow(5L, 7L);
        friendshipService.addFriendshipNow(5L, 8L);
        friendshipService.addFriendshipNow(5L, 9L);
    }
}
