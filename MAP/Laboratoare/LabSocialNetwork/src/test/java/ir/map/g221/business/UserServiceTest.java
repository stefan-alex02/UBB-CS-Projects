package ir.map.g221.business;

import ir.map.g221.domain.validation.FriendshipValidator;
import ir.map.g221.domain.validation.UserValidator;
import ir.map.g221.exceptions.NotFoundException;
import ir.map.g221.persistence.inmemoryrepos.FriendshipInMemoryRepo;
import ir.map.g221.persistence.inmemoryrepos.UserInMemoryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void addUser() {
        UserService userService = createuserService();
        addSampleUsers(userService);

        assertEquals(3, userService.calculateCommunities().size());
    }

    @Test
    void removeUser() {
        // 1) Having an empty user service, when deleting a non-existent user, throws exception.
        UserService userService = createuserService();

        Assertions.assertThrows(NotFoundException.class, () -> userService.removeUser(9L));

        // 2) Having a user service with users, when deleting a user, they no longer exist in the
        // communities, and in the friendship lists of ex-friends as well.
        addSampleUsers(userService);
        addSampleFriendships(userService);

        Assertions.assertEquals(2, userService.getFriendsOfUser(1L).size());
        Assertions.assertTrue(
                userService.getFriendsOfUser(1L)
                        .contains(userService.getUser(2L))
        );

        userService.removeUser(2L);

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(2L));
        Assertions.assertEquals(1, userService.calculateCommunities().size());
        Assertions.assertEquals(2, userService.mostSociableCommunity().size());

        Assertions.assertEquals(1, userService.getFriendsOfUser(1L).size());
        Assertions.assertTrue(
                userService.getFriendsOfUser(1L)
                        .contains(userService.getUser(3L))
        );
    }

    @Test
    void addFriendship() {
        UserService userService = createuserService();
        addSampleUsers(userService);

        userService.addFriendshipNow(1L, 2L);
    }

    @Test
    void removeFriendship() {
        UserService userService = createuserService();
        addSampleUsers(userService);

        Assertions.assertThrows(NotFoundException.class, () -> userService.removeFriendship(7L, 5L));
        Assertions.assertThrows(NotFoundException.class, () -> userService.removeFriendship(1L, 2L));
        userService.addFriendshipNow(1L, 2L);
        userService.removeFriendship(1L, 2L);
    }

    @Test
    void mostSociableCommunity() {
        UserService userService = createuserService();
        addSampleUsers(userService);

        userService.addFriendshipNow(1L, 3L);
        Assertions.assertEquals(2, userService.mostSociableCommunity().size());
    }

    private static UserService createuserService() {
        UserInMemoryRepo userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        FriendshipInMemoryRepo friendshipRepo = new FriendshipInMemoryRepo(FriendshipValidator.getInstance());
        return new UserService(friendshipRepo, userRepo);
    }

    private static void addSampleUsers(UserService userService) {
        userService.addUser("fn1", "ln1");
        userService.addUser("fn2", "ln2");
        userService.addUser("fn3", "ln3");
    }

    private static void addSampleFriendships(UserService userService) {
        userService.addFriendshipNow(1L, 2L);
        userService.addFriendshipNow(1L, 3L);
    }
}