package ir.map.g221.guisocialnetwork.business;

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

        userService.addUser("firstName1", "lastName1");
        userService.addUser("firstName2", "lastName2");
        userService.addUser("firstName3", "lastName3");
        userService.addUser("firstName4", "lastName4");
        userService.addUser("firstName5", "lastName5");
        userService.addUser("firstName6", "lastName6");
        userService.addUser("firstName7", "lastName7");
        userService.addUser("firstName8", "lastName8");
        userService.addUser("firstName9", "lastName9");

        userService.addUser("firstName10", "lastName10");
        userService.addUser("firstName11", "lastName11");

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
