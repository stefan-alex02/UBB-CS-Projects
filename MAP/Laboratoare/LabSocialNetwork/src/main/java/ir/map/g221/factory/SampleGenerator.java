package ir.map.g221.factory;

import ir.map.g221.business.UserService;

import java.time.LocalDateTime;

public class SampleGenerator {
    private final UserService userService;

    public SampleGenerator(UserService userService) {
        this.userService = userService;
    }

    public void generateSample() {
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

        userService.addFriendship(2L, 1L,
                LocalDateTime.of(2012, 12, 21, 22, 30));
        userService.addFriendshipNow(1L, 3L);
        userService.addFriendshipNow(3L, 4L);
        userService.addFriendshipNow(2L, 4L);

        userService.addFriendshipNow(5L, 6L);
        userService.addFriendshipNow(5L, 7L);
        userService.addFriendshipNow(5L, 8L);
        userService.addFriendshipNow(5L, 9L);
    }
}
