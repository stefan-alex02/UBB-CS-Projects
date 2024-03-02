package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.PasswordEncoder;
import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.persistence.inmemoryrepos.FriendshipInMemoryRepo;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import ir.map.g221.guisocialnetwork.utils.observer.Observable;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserService implements Observable {
    private final PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final PagingRepository<Long, User> userRepository;
    private final Set<Observer> observers;

    public UserService(PagingRepository<Long, User> userRepository,
                       PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.observers = Collections.newSetFromMap(new ConcurrentHashMap<>(0));
    }

    public void addUser(String username, String firstName, String lastName, String password) throws ValidationException {
        User userToAdd = new User(username, firstName, lastName, password);
        if (userRepository.save(userToAdd).isEmpty()) {
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

    public void updateUser(Long id, String username, String firstName, String lastName, String password) {
        User oldUser = getUser(id);
        User updatedUser = new User(id, username, firstName, lastName, password);
        userRepository.update(updatedUser).ifPresentOrElse(
                u -> {
                    throw new RuntimeException("Error while trying to update user.");
                },
                () -> notifyObservers(UserChangeEvent.of(ChangeEventType.UPDATE, updatedUser, oldUser)));
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
                    throw new NotFoundException("Failed to delete user.");
                }
        );
    }

    public Set<User> getAllUsers() {
        return ObjectTransformer.iterableToSet(userRepository.findAll());
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public int getUserCount() {
        return userRepository.getSize();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        observers.forEach(observer -> observer.update(event));
    }
}
