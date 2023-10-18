package ir.map.g221.factory;

import ir.map.g221.business.UserService;
import ir.map.g221.domain.validation.FriendshipValidator;
import ir.map.g221.domain.validation.UserValidator;
import ir.map.g221.persistence.in_memory_concrete_repos.FriendshipInMemoryRepo;
import ir.map.g221.persistence.in_memory_concrete_repos.UserInMemoryRepo;
import ir.map.g221.ui.UserConsole;
import ir.map.g221.ui.UserInterface;

public class Factory {
    private static Factory instance = null;

    private Factory() {
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public BuildContainer build() {
        UserInMemoryRepo userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        FriendshipInMemoryRepo friendshipRepo = new FriendshipInMemoryRepo(FriendshipValidator.getInstance());
        UserService userService = new UserService(friendshipRepo, userRepo);
        SampleGenerator sampleGenerator = new SampleGenerator(userService);
        UserInterface ui = new UserConsole(userService, sampleGenerator);
        return new BuildContainer(userService, ui, sampleGenerator);
    }
}
