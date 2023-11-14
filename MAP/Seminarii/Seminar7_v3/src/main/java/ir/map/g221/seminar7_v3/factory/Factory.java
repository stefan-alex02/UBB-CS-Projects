package ir.map.g221.seminar7_v3.factory;

import ir.map.g221.seminar7_v3.business.UserService;
import ir.map.g221.seminar7_v3.domain.entities.User;
import ir.map.g221.seminar7_v3.domain.validation.UserValidator;
import ir.map.g221.seminar7_v3.persistence.Repository;
import ir.map.g221.seminar7_v3.persistence.dbrepos.UserDBRepository;
import ir.map.g221.seminar7_v3.ui.UserConsole;
import ir.map.g221.seminar7_v3.ui.UserInterface;

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

        UserService userService = new UserService(userRepo);

        SampleGenerator sampleGenerator = new SampleGenerator(userService);
        UserInterface ui = new UserConsole(userService, sampleGenerator);

        return new BuildContainer(userService, ui, sampleGenerator);
    }
}
