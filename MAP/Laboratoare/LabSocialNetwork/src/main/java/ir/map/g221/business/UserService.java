package ir.map.g221.business;

import ir.map.g221.domain.entities.dtos.FriendshipDTO;
import ir.map.g221.domain.generaltypes.ObjectTransformer;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.domain.graphs.Edge;
import ir.map.g221.domain.graphs.UndirectedGraph;
import ir.map.g221.domain.Community;
import ir.map.g221.exceptions.ExistingEntityException;
import ir.map.g221.exceptions.NotFoundException;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.persistence.Repository;
import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        userRepository.save(new User(firstName, lastName));
    }

    public User getUser(Long id) throws NotFoundException {
        return userRepository
                .findOne(id)
                .orElseThrow(() ->
                    new NotFoundException("User could not be found.")
                );
    }

    public void removeUser(Long id) throws NotFoundException {
        Set<User> friendsOfRemovedUser = getFriendsOfUser(id);

        userRepository.delete(id).ifPresentOrElse(user ->
                friendsOfRemovedUser.forEach(friend ->
                        friendshipRepository.delete(UnorderedPair.ofAscending(id, friend.getId()))
        ), () -> {
            throw new NotFoundException("User could not be found.");
        });
    }

    public void addFriendship(Long id1, Long id2, LocalDateTime friendsFromDate) {
        User user1 = getUser(id1);
        User user2 = getUser(id2);

        if (getFriendsOfUser(user1.getId()).contains(user2) ||
                getFriendsOfUser(user2.getId()).contains(user1)) {
            throw new ExistingEntityException("Friendship already exists.");
        }

        Friendship friendship = new Friendship(user1, user2, friendsFromDate);
        friendshipRepository.save(friendship);
    }

    public void addFriendshipNow(Long id1, Long id2) {
        addFriendship(id1, id2, LocalDateTime.now());
    }

    public void removeFriendship(Long id1, Long id2) throws NotFoundException {
        User user1 = getUser(id1);
        User user2 = getUser(id2);

        if (friendshipRepository.delete(new Friendship(user1, user2).getId())
                .isEmpty()) {
            throw new NotFoundException("The specified friendship does not exist.");
        }
    }

    public Set<User> getFriendsOfUser(Long id) throws NotFoundException {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
            new NotFoundException("The specified friendship does not exist.")
        );
        return ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                .stream()
                .filter(friendship -> friendship.hasUser(foundUser))
                .map(friendship -> friendship.theOtherFriend(foundUser))
                .collect(Collectors.toSet());
    }

    public Set<FriendshipDTO> getFriendshipDTOsOfUserInYearMonth(Long id, YearMonth yearMonth) throws NotFoundException {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified friendship does not exist.")
        );
        return ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                .stream()
                .filter(friendship -> friendship.hasUser(foundUser) &&
                        YearMonth.from(friendship.getFriendsFromDate()).equals(yearMonth))
                .map(friendship -> FriendshipDTO.of(friendship, foundUser))
                .collect(Collectors.toSet());
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
                .map(fr -> Edge.of(fr.getFirstUser(),fr.getSecondUser()))
                .toList()
        );
        return graph;
    }
}
