package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.validation.FriendshipValidator;
import ir.map.g221.guisocialnetwork.domain.validation.UserValidator;
import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.persistence.inmemoryrepos.FriendshipInMemoryRepo;
import ir.map.g221.guisocialnetwork.persistence.inmemoryrepos.UserInMemoryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static UserInMemoryRepo userRepo;
    private static FriendshipInMemoryRepo friendshipRepo;
    private static CommunityHandler communityHandler;

    @Test
    void addUser() {
        initialiseRepos();
        UserService userService = createUserService();
        CommunityHandler communityHandler = new CommunityHandler(userRepo, friendshipRepo);
        addSampleUsers(userService);

        assertEquals(3, communityHandler.calculateCommunities().size());
    }

    @Test
    void removeUser() {
        initialiseRepos();
        // 1) Having an empty user service, when deleting a non-existent user, throws exception.
        UserService userService = createUserService();
        FriendshipService friendshipService = createFriendshipService();
        CommunityHandler communityHandler = new CommunityHandler(userRepo, friendshipRepo);

        Assertions.assertThrows(NotFoundException.class, () -> userService.removeUser(9L));

        // 2) Having a user service with users, when deleting a user, they no longer exist in the
        // communities, and in the friendship lists of ex-friends as well.
        addSampleUsers(userService);
        addSampleFriendships(friendshipService);

        Assertions.assertEquals(2, friendshipService.getFriendsOfUser(1L).size());
        Assertions.assertTrue(
                friendshipService.getFriendsOfUser(1L)
                        .contains(userService.getUser(2L))
        );

        userService.removeUser(2L);

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(2L));
        Assertions.assertEquals(1, communityHandler.calculateCommunities().size());
        Assertions.assertEquals(2, communityHandler.mostSociableCommunity().size());

        Assertions.assertEquals(1, friendshipService.getFriendsOfUser(1L).size());
        Assertions.assertTrue(
                friendshipService.getFriendsOfUser(1L)
                        .contains(userService.getUser(3L))
        );
    }

    @Test
    void addFriendship() {
        initialiseRepos();
        UserService userService = createUserService();
        FriendshipService friendshipService = createFriendshipService();
        addSampleUsers(userService);

        friendshipService.addFriendshipNow(1L, 2L);
    }

    @Test
    void removeFriendship() {
        initialiseRepos();
        UserService userService = createUserService();
        FriendshipService friendshipService = createFriendshipService();
        addSampleUsers(userService);

        Assertions.assertThrows(NotFoundException.class, () -> friendshipService.removeFriendship(7L, 5L));
        Assertions.assertThrows(NotFoundException.class, () -> friendshipService.removeFriendship(1L, 2L));
        friendshipService.addFriendshipNow(1L, 2L);
        friendshipService.removeFriendship(1L, 2L);
    }

    @Test
    void mostSociableCommunity() {
        initialiseRepos();
        UserService userService = createUserService();
        FriendshipService friendshipService = createFriendshipService();
        CommunityHandler communityHandler = new CommunityHandler(userRepo, friendshipRepo);
        addSampleUsers(userService);

        friendshipService.addFriendshipNow(1L, 3L);
        Assertions.assertEquals(2, communityHandler.mostSociableCommunity().size());
    }

    private static void initialiseRepos() {
        userRepo = new UserInMemoryRepo(UserValidator.getInstance());
        friendshipRepo = new FriendshipInMemoryRepo(FriendshipValidator.getInstance());
        communityHandler = new CommunityHandler(userRepo, friendshipRepo);
    }

    private static UserService createUserService() {
        return new UserService(userRepo, friendshipRepo, communityHandler);
    }

    private static FriendshipService createFriendshipService() {
        return new FriendshipService(userRepo, friendshipRepo, communityHandler);
    }

    private static void addSampleUsers(UserService userService) {
        userService.addUser("fn1", "ln1");
        userService.addUser("fn2", "ln2");
        userService.addUser("fn3", "ln3");
    }

    private static void addSampleFriendships(FriendshipService friendshipService) {
        friendshipService.addFriendshipNow(1L, 2L);
        friendshipService.addFriendshipNow(1L, 3L);
    }
}