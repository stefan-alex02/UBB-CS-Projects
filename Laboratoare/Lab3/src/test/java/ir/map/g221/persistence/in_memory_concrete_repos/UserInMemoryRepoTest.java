package ir.map.g221.persistence.in_memory_concrete_repos;

import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.validation.UserValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserInMemoryRepoTest {

    @Test
    void add() {
        User u1 = new User(2L, "firstName1", "lastName1");
        User u2 = new User(5L, "firstName2", "lastName2");
        User u3 = new User(7L, "firstName3", "lastName3");

        UserInMemoryRepo userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        userRepo.add(u1);
        userRepo.add(u2);
        userRepo.add(u3);

        assert(userRepo.getSize() == 3);
    }
}