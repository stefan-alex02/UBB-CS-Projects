package ir.map.g221.factory;

import ir.map.g221.business.UserService;
import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.domain.validation.FriendshipValidator;
import ir.map.g221.domain.validation.UserValidator;
import ir.map.g221.persistence.Repository;
import ir.map.g221.persistence.dbrepos.FriendshipDBRepository;
import ir.map.g221.persistence.dbrepos.UserDBRepository;
import ir.map.g221.persistence.inmemoryrepos.FriendshipInMemoryRepo;
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

        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "postgres";

        Repository<Long, User> userRepo =
                new UserDBRepository(url, username, password, UserValidator.getInstance());
        Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepo =
                new FriendshipDBRepository(url, username, password, FriendshipValidator.getInstance());
        UserService userService = new UserService(friendshipRepo, userRepo);
        SampleGenerator sampleGenerator = new SampleGenerator(userService);
        UserInterface ui = new UserConsole(userService, sampleGenerator);
        return new BuildContainer(userService, ui, sampleGenerator);
    }
}
