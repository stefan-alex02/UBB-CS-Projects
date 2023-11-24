package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.EventType;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FriendshipService implements Observable<FriendshipChangeEvent> {
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private final Set<Observer> observers;

    public FriendshipService(Repository<Long, User> userRepository,
                             Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        observers = new HashSet<>();
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

    private Stream<FriendshipDetails> getFriendshipDetailsStreamOfUser(Long id) {
        User foundUser = userRepository.findOne(id).orElseThrow(() ->
                new NotFoundException("The specified user does not exist.")
        );
        return ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
                .stream()
                .filter(friendship -> friendship.hasUser(foundUser))
                .map(friendship -> FriendshipDetails.of(friendship, foundUser));
    }

    public Set<FriendshipDetails> getFriendshipDetailsInYearMonth(Long id, YearMonth yearMonth) throws NotFoundException {
        return getFriendshipDetailsStreamOfUser(id)
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
    public void notifyObservers(FriendshipChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
