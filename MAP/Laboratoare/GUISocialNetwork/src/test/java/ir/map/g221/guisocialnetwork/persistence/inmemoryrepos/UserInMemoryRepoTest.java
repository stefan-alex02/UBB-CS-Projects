package ir.map.g221.guisocialnetwork.persistence.inmemoryrepos;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

class UserInMemoryRepoTest {

    @Test
    void add() {
        User u1 = new User(2L, "username1", "firstName1", "lastName1", "12345");
        User u2 = new User(5L, "username2", "firstName2", "lastName2", "12345");
        User u3 = new User(7L, "username3", "firstName3", "lastName3", "12345");

        UserInMemoryRepo userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);

        assert(userRepo.getSize() == 3);
    }

    @Test
    void update() {User u1 = new User(2L, "username1", "firstName1", "lastName1", "12345");
        User u2 = new User(5L, "username2", "firstName2", "lastName2", "12345");
        User u3 = new User(7L, "username3", "firstName3", "lastName3", "12345");
        User newU1 = new User(2L, "username1", "firstName1", "lastName1", "12345");

        UserInMemoryRepo userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        userRepo.save(u1);
        userRepo.save(u2);

        Assertions.assertEquals(Optional.of(u3), userRepo.update(u3));
        Assertions.assertEquals(Optional.empty(), userRepo.update(newU1));

        Assertions.assertEquals(newU1.getFirstName(),
                userRepo.findOne(newU1.getId()).orElseThrow().getFirstName());
        Assertions.assertEquals(newU1.getLastName(),
                userRepo.findOne(newU1.getId()).orElseThrow().getLastName());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> userRepo.findOne(u3.getId()).orElseThrow());


        assert(userRepo.getSize() == 2);
    }
}