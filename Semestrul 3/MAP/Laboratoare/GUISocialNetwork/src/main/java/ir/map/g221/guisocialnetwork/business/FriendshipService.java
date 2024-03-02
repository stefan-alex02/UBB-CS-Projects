package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
import ir.map.g221.guisocialnetwork.persistence.customqueries.FriendshipDetailsOfUserQuery;
import ir.map.g221.guisocialnetwork.persistence.customqueries.NonFriendsOfUserQuery;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.FriendshipChangeEvent;
import ir.map.g221.guisocialnetwork.exceptions.ExistingEntityException;
import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.generictypes.Pair;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import ir.map.g221.guisocialnetwork.utils.observer.Observable;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FriendshipService implements Observable {
    private final PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final PagingRepository<Long, User> userRepository;
    private final Set<Observer> observers;

    public FriendshipService(PagingRepository<Long, User> userRepository,
                             PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        observers = Collections.newSetFromMap(new ConcurrentHashMap<>(0));
    }

    public void addFriendship(Long id1, Long id2, LocalDateTime friendsFromDate) {
        Pair<User, User> pairOfUsers = getPairOfUsers(id1, id2);

        User user1 = pairOfUsers.getFirst();
        User user2 = pairOfUsers.getSecond();

        if (getFriendsOfUser(user1.getId()).contains(user2) ||
                getFriendsOfUser(user2.getId()).contains(user1)) {
            throw new ExistingEntityException("Friendship already exists.");
        }

        Friendship friendship = new Friendship(user1, user2, friendsFromDate);
        if (friendshipRepository.save(friendship).isEmpty()) {
            notifyObservers(FriendshipChangeEvent.ofNewData(ChangeEventType.ADD, friendship));
        }
    }

    public void addFriendshipNow(Long id1, Long id2) {
        addFriendship(id1, id2, LocalDateTime.now());
    }

    public void removeFriendship(Long id1, Long id2) throws NotFoundException {
        Pair<User, User> pairOfUsers = getPairOfUsers(id1, id2);

        User user1 = pairOfUsers.getFirst();
        User user2 = pairOfUsers.getSecond();

        friendshipRepository.delete(new Friendship(user1, user2).getId())
            .ifPresentOrElse(
                deletedFriendship -> notifyObservers(
                        FriendshipChangeEvent.ofOldData(ChangeEventType.DELETE, deletedFriendship)
                )
            , () -> {
                throw new NotFoundException("The specified friendship does not exist.");
            }
        );
    }

    private @NotNull Pair<User, User> getPairOfUsers(Long id1, Long id2) throws NotFoundException {
        Optional<User> optionalUser1 = userRepository.findOne(id1);
        Optional<User> optionalUser2 = userRepository.findOne(id2);

        if (optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
            throw new NotFoundException("At least one user could not be found.");
        }

        return Pair.of(optionalUser1.get(), optionalUser2.get());
    }

    public Set<User> getNonFriendsOfUser(Long id) throws NotFoundException {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );

        return ObjectTransformer.iterableToCollection(userRepository.findAll())
                .stream()
                .filter(user -> !foundUser.hasFriend(user) && !user.equals(foundUser))
                .collect(Collectors.toSet());
    }

    public Page<User> getNonFriendsOfUser(Long id, Pageable pageable) throws NotFoundException {
        userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return userRepository.findAllWhere(new NonFriendsOfUserQuery(id), pageable);
    }

    public Integer getNumberOfNonFriendsOfUser(Long id) {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return Math.toIntExact(ObjectTransformer.iterableToCollection(userRepository.findAll())
                .stream()
                .filter(user -> !foundUser.hasFriend(user) && !user.equals(foundUser))
                .count());
    }

    public Set<User> getFriendsOfUser(Long id) throws NotFoundException {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                .stream()
                .filter(friendship -> friendship.hasUser(foundUser))
                .map(friendship -> friendship.theOtherFriend(foundUser))
                .collect(Collectors.toSet());
    }

    public Set<FriendshipDetails> getFriendshipDetailsOfUser(Long id) {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                .stream()
                .filter(friendship -> friendship.hasUser(foundUser))
                .map(friendship -> FriendshipDetails.of(friendship, foundUser))
                .collect(Collectors.toSet());
    }

    public Integer getNumberOfFriendsOfUser(Long id) {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return Math.toIntExact(ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                .stream()
                .filter(friendship -> friendship.hasUser(foundUser))
                .map(friendship -> friendship.theOtherFriend(foundUser))
                .count());
    }

    public Page<FriendshipDetails> getFriendshipDetailsOfUser(Long id, Pageable pageable) throws NotFoundException {
        userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return friendshipRepository.findAllWhere(
                new FriendshipDetailsOfUserQuery(id), pageable);
    }

    public Set<FriendshipDetails> getFriendshipDetailsInYearMonth(Long id, YearMonth yearMonth) throws NotFoundException {
        return getFriendshipDetailsOfUser(id)
                .stream()
                .filter(friendshipDetails ->
                        YearMonth.from(friendshipDetails.getFriendsFromDate()).equals(yearMonth))
                .collect(Collectors.toSet());
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
