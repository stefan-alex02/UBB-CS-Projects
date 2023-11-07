package ir.map.g221.business;

import ir.map.g221.domain.generaltypes.ObjectTransformer;
import ir.map.g221.domain.graphs.Edge;
import ir.map.g221.domain.graphs.UndirectedGraph;
import ir.map.g221.domain.Community;
import ir.map.g221.exceptions.ExistingEntityException;
import ir.map.g221.exceptions.NotFoundException;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.persistence.Repository;
import ir.map.g221.persistence.inmemoryrepos.FriendshipInMemoryRepo;
import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.persistence.inmemoryrepos.UserInMemoryRepo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private UndirectedGraph<User> usersGraph;

    public UserService(Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository,
                       Repository<Long, User> userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.usersGraph = null;
    }

    public void addUser(String firstName, String lastName) throws ValidationException {
        // The id is unique and chosen by incrementally checking its availability :
        Long id = 1L;
        boolean availableId = false;
        while(!availableId) {
            if (userRepository.findOne(id).isEmpty()) {
                availableId = true;
            }
            else {
                id++;
            }
        }
        userRepository.save(new User(id, firstName, lastName));
    }

    public User getUser(Long id) throws NotFoundException {
        return userRepository
                .findOne(id)
                .orElseThrow(() ->
                        new NotFoundException("User could not be found.")
                );
    }

    public void removeUser(Long id) throws NotFoundException {
        userRepository.delete(id).ifPresentOrElse(user ->
            user.getFriends().forEach(friend -> {
                friend.removeFriendById(id);
                userRepository.update(friend);
                friendshipRepository.delete(UnorderedPair.of(id, friend.getId()));
            }
        ), () -> {
            throw new NotFoundException("User could not be found.");
        });
    }

    public void addFriendship(Long id1, Long id2) {
        User user1 = getUser(id1);
        User user2 = getUser(id2);

        if (!user1.addFriend(user2) || !user2.addFriend(user1)) {
            throw new ExistingEntityException("Friendship already exists.");
        }

        userRepository.update(user1);
        userRepository.update(user2);

        Friendship friendship = new Friendship(UnorderedPair.of(id1, id2), LocalDateTime.now());
        friendshipRepository.save(friendship);
    }

    public void removeFriendship(Long id1, Long id2) throws NotFoundException {
        User user1 = getUser(id1);
        User user2 = getUser(id2);

        Friendship friendship = new Friendship(UnorderedPair.of(id1, id2));
        if (friendshipRepository.delete(friendship.getId()).isEmpty() ||
                !user1.removeFriendById(id2) ||
                !user2.removeFriendById(id1)) {
            throw new NotFoundException("The specified friendship does not exist.");
        }

        userRepository.update(user1);
        userRepository.update(user2);
    }

    public List<Community> calculateCommunities() {
        return createGraphFromUsers()
                .getAllComponents().stream()
                .map(Community::new)
                .toList();
    }

    public Community mostSociableCommunity() {
        return new Community(createGraphFromUsers().getComponentWithLongestPath());
    }

    private UndirectedGraph<User> createGraphFromUsers() {
        UndirectedGraph<User> graph = new UndirectedGraph<>(
                ObjectTransformer.iterableToSet(userRepository.findAll())
        );

        graph.tryAddEdges(ObjectTransformer.iterableToCollection(friendshipRepository.findAll()).stream()
                .map(fr -> {
                    Optional<User> user1 = userRepository.findOne(fr.getId().getFirst());
                    Optional<User> user2 = userRepository.findOne(fr.getId().getSecond());
                    return Edge.of(user1.orElse(null),user2.orElse(null));
                }).toList()
        );
        return graph;
    }
}
