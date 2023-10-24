package ir.map.g221.business;

import ir.map.g221.domain.graphs.Edge;
import ir.map.g221.domain.graphs.UndirectedGraph;
import ir.map.g221.domain.Community;
import ir.map.g221.exceptions.ExistingEntityException;
import ir.map.g221.exceptions.NotFoundException;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.persistence.inmemoryrepos.FriendshipInMemoryRepo;
import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.persistence.inmemoryrepos.UserInMemoryRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final FriendshipInMemoryRepo friendshipRepository;
    private final UserInMemoryRepo userRepository;

    public UserService(FriendshipInMemoryRepo friendshipRepository,
                       UserInMemoryRepo userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public void addUser(String firstName, String lastName) throws ValidationException {
        // The id is unique and chosen by incrementally checking its availability :
        Long id = 1L;
        boolean availableId = false;
        while(!availableId) {
            if (userRepository.findOne(id) == null) {
                availableId = true;
            }
            else {
                id++;
            }
        }
        userRepository.add(new User(id, firstName, lastName));
    }

    public void removeUser(Long id) throws NotFoundException {
        User userToRemove = userRepository.findOne(id);

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
        var u1 = userRepository.findOne(id1);
        var u2 = userRepository.findOne(id2);

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

    public void removeFriendship(Long id1, Long id2) throws NotFoundException {
        Optional<User> u1 = userRepository.findOne(id1);
        Optional<User> u2 = userRepository.findOne(id2);

        if (u1.isEmpty() || u2.isEmpty()) {
            throw new NotFoundException("At least one of the users could not be found.");
        }

        if (!u1.get().removeFriendById(id2) || !u2.get().removeFriendById(id1)) {
            throw new NotFoundException("The specified friendship does not exist.");
        }

        userRepository.update(u1.get());
        userRepository.update(u2.get());

        Friendship friendship = new Friendship(UnorderedPair.of(id1, id2), LocalDateTime.now());
        friendshipRepository.delete(friendship.getId());
    }

    public List<Community> calculateCommunities() {
        List<Community> communities = new ArrayList<>();
        UndirectedGraph<User> graph = new UndirectedGraph<>(new HashSet<>(userRepository.findAll()));

        graph.tryAddEdges(friendshipRepository.findAll().stream()
                .map(fr -> {
                    Optional<User> user1 = userRepository.findOne(fr.getId().getFirst());
                    Optional<User> user2 = userRepository.findOne(fr.getId().getSecond());
                    return Edge.of(user1.orElse(null),user2.orElse(null));
                }).toList()
        );

        graph.getAllComponents().forEach(
                component -> communities.add(new Community(component))
        );
        return communities;
    }

    public Community mostSociableCommunity() {
        UndirectedGraph<User> graph = new UndirectedGraph<>(new HashSet<>(userRepository.findAll()));

        graph.tryAddEdges(friendshipRepository.findAll().stream()
                .map(fr -> {
                    Optional<User> user1 = userRepository.findOne(fr.getId().getFirst());
                    Optional<User> user2 = userRepository.findOne(fr.getId().getSecond());
                    return Edge.of(user1.orElse(null),user2.orElse(null));
                }).toList()
        );

        return new Community(graph.getComponentWithLongestPath());
    }
}
