package ir.map.g221.business;

import ir.map.g221.domain.validation.FriendshipValidator;
import ir.map.g221.domain.validation.UserValidator;
import ir.map.g221.persistence.in_memory_concrete_repos.FriendshipInMemoryRepo;
import ir.map.g221.persistence.in_memory_concrete_repos.UserInMemoryRepo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void addUser() {
        UserInMemoryRepo userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        FriendshipInMemoryRepo friendshipRepo = new FriendshipInMemoryRepo(FriendshipValidator.getInstance());
        UserService userService = new UserService(friendshipRepo, userRepo);

        userService.addUser("fn1", "ln1");
        userService.addUser("fn2", "ln2");
        userService.addUser("fn3", "ln3");

        assertEquals(3, userService.calculateCommunities().size());
    }
}