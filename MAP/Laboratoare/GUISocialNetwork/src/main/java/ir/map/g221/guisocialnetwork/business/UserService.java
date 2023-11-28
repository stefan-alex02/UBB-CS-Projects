package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.inmemoryrepos.FriendshipInMemoryRepo;
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
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private final Set<Observer> observers;

    public UserService(Repository<Long, User> userRepository,
                       Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.observers = Collections.newSetFromMap(new ConcurrentHashMap<>(0));
    }

    public void addUser(String firstName, String lastName) throws ValidationException {
        User userToAdd = new User(firstName, lastName);
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

    public void updateUser(Long id, String firstName, String lastName) {
        User oldUser = getUser(id);
        User updatedUser = new User(id, firstName, lastName);
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
