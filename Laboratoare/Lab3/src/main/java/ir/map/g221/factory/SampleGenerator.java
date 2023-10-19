package ir.map.g221.factory;

import ir.map.g221.business.UserService;

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
        userService.addFriendship(1L, 5L);
    }
}
