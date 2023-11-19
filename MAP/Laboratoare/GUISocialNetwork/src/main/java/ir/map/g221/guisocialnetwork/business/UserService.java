package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.inmemoryrepos.FriendshipInMemoryRepo;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.graphs.Edge;
import ir.map.g221.guisocialnetwork.utils.graphs.UndirectedGraph;
import ir.map.g221.guisocialnetwork.domain.Community;
import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.utils.observer.Observable;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService implements Observable<UserChangeEvent> {
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private final Set<Observer<UserChangeEvent>> observers;

    public UserService(Repository<Long, User> userRepository,
                       Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.observers = new HashSet<>();
    }

    public void addUser(String firstName, String lastName) throws ValidationException {
        User userToAdd = new User(firstName, lastName);
        if(userRepository.save(userToAdd).isEmpty()) {
            notifyObservers(UserChangeEvent.ofNewData(ChangeEventType.ADD, userToAdd));
        }
    }

    public User getUser(Long id) throws NotFoundException {
        return userRepository
                .findOne(id)
                .orElseThrow(() ->
                    new NotFoundException("User could not be found.")
                );
    }

    public void updateUser(Long id, String firstName, String lastName) {
        User userToUpdate = new User(id, firstName, lastName);
        userRepository.update(userToUpdate).ifPresentOrElse(
                u -> {
                    throw new RuntimeException("Error while trying to update user.");
                },
                () -> notifyObservers(UserChangeEvent.ofNewData(ChangeEventType.ADD, userToUpdate)));
    }

    public void removeUser(Long id) throws NotFoundException {
        // If it's a FriendshipInMemoryRepo, delete the friendships as well:
        if (friendshipRepository instanceof FriendshipInMemoryRepo) {
            userRepository.findOne(id).ifPresentOrElse(user ->
                            ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                                    .stream()
                                    .filter(friendship -> friendship.hasUser(user))
                                    .forEach(friendship -> friendshipRepository.delete(friendship.getId()))
                    , () -> {
                        throw new NotFoundException("User could not be found.");
                    });
        }
        userRepository.delete(id).ifPresentOrElse(
                deletedUser -> notifyObservers(
                        UserChangeEvent.ofOldData(ChangeEventType.DELETE, deletedUser)
                ),
                () -> {
                    throw new NotFoundException("User could not be found.");
                }
        );
    }

    public Set<User> getAllUsers() {
        return ObjectTransformer.iterableToSet(userRepository.findAll());
    }

    public List<Community> calculateCommunities() {
        return createGraphFromUsers()
                .getComponents().stream()
                .map(Community::new)
                .toList();
    }

    public Community mostSociableCommunity() {
        return new Community(createGraphFromUsers().getComponentWithLongestPath());
    }

    private UndirectedGraph<User> createGraphFromUsers() {
        return new UndirectedGraph<>(
                ObjectTransformer.iterableToSet(userRepository.findAll()),
                ObjectTransformer.iterableToCollection(friendshipRepository.findAll()).stream()
                        .map(fr -> Edge.of(fr.getFirstUser(),fr.getSecondUser()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(UserChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
