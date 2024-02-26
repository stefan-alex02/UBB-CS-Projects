package ir.map.g221.seminar7_v3.factory;

import ir.map.g221.seminar7_v3.business.UserService;

public class SampleGenerator {
    private final UserService userService;
    private boolean alreadyGenerated = false;

    public SampleGenerator(UserService userService) {
        this.userService = userService;
    }

    public void generateSample() {
        if (alreadyGenerated) {
            throw new RuntimeException("Samples have already been generated");
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
    }
}
