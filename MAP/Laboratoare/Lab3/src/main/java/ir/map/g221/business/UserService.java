package ir.map.g221.business;

import ir.map.g221.domain.graphs.UnorderedGraph;
import ir.map.g221.domain.Community;
import ir.map.g221.exceptions.ExistingEntityException;
import ir.map.g221.exceptions.NotFoundException;
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

    public void addUser(String firstName, String lastName) throws ValidationException {
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
        userRepository.add(new User(id, firstName, lastName));
    }

    public void removeUser(Long id) throws IllegalArgumentException {
        User userToRemove = userRepository.getById(id);

        if (userToRemove == null) {
            throw new NotFoundException("User could not be found.");
        }

        for (User friend: userToRemove.getFriends() ) {
            friend.removeFriendById(id);
            userRepository.update(friend);
            friendshipRepository.delete(UnorderedPair.of(id, friend.getId()));
        }
        userRepository.delete(id);
    }

    public void addFriendship(Long id1, Long id2) {
        var u1 = userRepository.getById(id1);
        var u2 = userRepository.getById(id2);

        if (u1 == null || u2 == null) {
            throw new NotFoundException("At least one of the users could not be found.");
        }

        if (!u1.addFriend(u2) || !u2.addFriend(u1)) {
            throw new ExistingEntityException("Friendship already exists.");
        }

        userRepository.update(u1);
        userRepository.update(u2);

        Friendship friendship = new Friendship(UnorderedPair.of(id1, id2), LocalDateTime.now());
        friendshipRepository.add(friendship);
    }

    public void removeFriendship(Long id1, Long id2) {
        var u1 = userRepository.getById(id1);
        var u2 = userRepository.getById(id2);

        if (u1 == null || u2 == null) {
            throw new NotFoundException("At least one of the users could not be found.");
        }

        if (!u1.removeFriendById(id2) || !u2.removeFriendById(id1)) {
            throw new NotFoundException("The specified friendship does not exist.");
        }

        userRepository.update(u1);
        userRepository.update(u2);

        Friendship friendship = new Friendship(UnorderedPair.of(id1, id2), LocalDateTime.now());
        friendshipRepository.delete(friendship.getId());
    }

    public List<Community> calculateCommunities() {
        List<Community> communities = new ArrayList<>();
        UnorderedGraph<User> g = new UnorderedGraph<>(userRepository.getAll());

        g.generateEdges(friendshipRepository.getAll().stream()
                .map(fr -> new UnorderedPair<>(
                        userRepository.getById(fr.getId().getFirst()),
                        userRepository.getById(fr.getId().getSecond())))
                .toList());

        g.getAllComponents().forEach(comp -> communities.add(new Community(comp.getNodes().stream().toList())));
        return communities;
    }

    public Community mostSociableCommunity() {
        UnorderedGraph<User> g = new UnorderedGraph<>(userRepository.getAll());
        var componentWithLongestPath = g.getComponentWithLongestPath();
        return new Community(componentWithLongestPath.getNodes().stream().toList(),
                componentWithLongestPath.getLongestPath());
    }
}
