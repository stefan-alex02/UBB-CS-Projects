package ir.map.g221.business;

import ir.map.g221.domain.Community;
import ir.map.g221.domain.UnorderedGraph;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.persistence.InMemoryRepository;
import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.general_types.UnorderedPair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final InMemoryRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final InMemoryRepository<Long, User> userRepository;

    public UserService(InMemoryRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository,
                       InMemoryRepository<Long, User> userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public boolean addUser(String firstName, String secondName) throws ValidationException {
        // The id is unique and chosen by incrementally checking its availability :
        Long id = 1L;
        boolean availableId = false;
        while(!availableId) {
            if (userRepository.getById(id) == null) {
                availableId = true;
            }
            else {
                id++;
            }
        }
        return (userRepository.add(new User(id, firstName, secondName)) != null);
    }

    public boolean removeUser(Long id) throws IllegalArgumentException {
        User userToRemove = userRepository.getById(id);
        if (userToRemove == null) {
            return false;
        }
        for (User friend: userToRemove.getFriends() ) {
            friend.removeFriendById(id);
            userRepository.update(friend);
            friendshipRepository.delete(UnorderedPair.create(id, friend.getId()));
        }
        userRepository.delete(id);
        return true;
    }

    public boolean addFriendship(Long id1, Long id2) {
        var u1 = userRepository.getById(id1);
        var u2 = userRepository.getById(id2);

        if (u1 == null || u2 == null) {
            return false;
        }

        u1.addFriend(u2);
        u2.addFriend(u1);

        userRepository.update(u1);
        userRepository.update(u2);

        Friendship friendship = new Friendship(UnorderedPair.create(id1, id2), LocalDateTime.now());
        friendshipRepository.add(friendship);

        return true;
    }

    public boolean removeFriendship(Long id1, Long id2) {
        var u1 = userRepository.getById(id1);
        var u2 = userRepository.getById(id2);

        if (u1 == null || u2 == null) {
            return false;
        }

        u1.removeFriendById(id2);
        u2.removeFriendById(id1);

        userRepository.update(u1);
        userRepository.update(u2);

        Friendship friendship = new Friendship(UnorderedPair.create(id1, id2), LocalDateTime.now());
        friendshipRepository.delete(friendship.getId());

        return true;
    }

    public List<Community> calculateCommunities() {
        List<Community> communities = new ArrayList<>();
        UnorderedGraph<User> g = new UnorderedGraph<User>(userRepository.getSize(), userRepository.getAll());

        g.generateEdges(friendshipRepository.getAll().stream()
                .map(fr -> new UnorderedPair<>(
                        userRepository.getById(fr.getId().getFirst()),
                        userRepository.getById(fr.getId().getSecond())))
                .toList());

        g.getAllComponents().forEach(comp -> communities.add(new Community(comp)));
        return communities;
    }

//    public Community mostSociableCommunity() {
//        UnorderedGraph<User> g = new UnorderedGraph<User>(userRepository.getSize(), userRepository.getAll());
//
//        return new Community();
//    }
}
