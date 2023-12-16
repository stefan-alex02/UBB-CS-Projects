package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequestStatus;
import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.exceptions.ExistingEntityException;
import ir.map.g221.guisocialnetwork.exceptions.InvalidEntityException;
import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.customqueries.PendingFriendRequestsOfUserQuery;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.FriendRequestChangeEvent;
import ir.map.g221.guisocialnetwork.utils.events.FriendshipChangeEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.generictypes.Pair;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import ir.map.g221.guisocialnetwork.utils.observer.Observable;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FriendRequestService implements Observable {
    private final PagingRepository<Long, FriendRequest> friendRequestRepository;
    private final PagingRepository<Long, User> userRepository;
    private final PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final Set<Observer> observers;

    public FriendRequestService(PagingRepository<Long, FriendRequest> friendRequestRepository,
                                PagingRepository<Long, User> userRepository,
                                PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.observers = Collections.newSetFromMap(new ConcurrentHashMap<>(0));
    }

    private @NotNull Pair<User, User> getPairOfUsers(Long idFrom, Long idTo) throws NotFoundException {
        Optional<User> optionalUserFrom = userRepository.findOne(idFrom);
        Optional<User> optionalUserTo = userRepository.findOne(idTo);

        if (optionalUserFrom.isEmpty() || optionalUserTo.isEmpty()) {
            throw new NotFoundException("At least one user could not be found.");
        }

        return Pair.of(optionalUserFrom.get(), optionalUserTo.get());
    }

    public void sendFriendRequestNow(Long idFrom, Long idTo) throws ValidationException, ExistingEntityException {
        Pair<User, User> pairOfUsers = getPairOfUsers(idFrom, idTo);

        User userFrom = pairOfUsers.getFirst();
        User userTo = pairOfUsers.getSecond();

        if (friendshipRepository.findOne(UnorderedPair.ofAscending(userFrom.getId(), userTo.getId())).isPresent()) {
            throw new NotFoundException("Cannot send friend request, friendship already exists.");
        }

        if (ObjectTransformer.iterableToCollection(friendRequestRepository.findAll())
                .stream()
                .anyMatch(friendRequest ->
                        Objects.equals(friendRequest.getFrom().getId(), idFrom) &&
                        Objects.equals(friendRequest.getTo().getId(), idTo) &&
                        friendRequest.getStatus() == FriendRequestStatus.PENDING)) {
            throw new ExistingEntityException("A friend request was already sent to this user.");
        }

        if (ObjectTransformer.iterableToCollection(friendRequestRepository.findAll())
                .stream()
                .anyMatch(friendRequest ->
                        Objects.equals(friendRequest.getFrom().getId(), idTo) &&
                                Objects.equals(friendRequest.getTo().getId(), idFrom) &&
                                friendRequest.getStatus() == FriendRequestStatus.PENDING)) {
            throw new ExistingEntityException("There is already a friend request coming from this user.");
        }

        FriendRequest friendRequestToAdd = new FriendRequest(userFrom, userTo,
                FriendRequestStatus.PENDING, LocalDateTime.now());
        if (friendRequestRepository.save(friendRequestToAdd).isEmpty()) {
            notifyObservers(FriendRequestChangeEvent.ofNewData(ChangeEventType.ADD, friendRequestToAdd));
        }
    }

    public Set<FriendRequest> getPendingFriendRequests(Long ofId) {
        if (userRepository.findOne(ofId).isEmpty()) {
            throw new NotFoundException("Specified user could not be found.");
        }

        return ObjectTransformer.iterableToCollection(friendRequestRepository.findAll())
                .stream()
                .filter(friendRequest ->
                        Objects.equals(friendRequest.getTo().getId(), ofId) &&
                        friendRequest.getStatus() == FriendRequestStatus.PENDING)
                .collect(Collectors.toSet());
    }

    public Page<FriendRequest> getPendingFriendRequests(Long ofId, Pageable pageable) {
        if (userRepository.findOne(ofId).isEmpty()) {
            throw new NotFoundException("Specified user could not be found.");
        }

        return friendRequestRepository.findAllWhere(new PendingFriendRequestsOfUserQuery(ofId), pageable);
    }

    public int getNumberOfPendingFriendRequests(Long ofId) {
        if (userRepository.findOne(ofId).isEmpty()) {
            throw new NotFoundException("Specified user could not be found.");
        }

        return Math.toIntExact(ObjectTransformer.iterableToCollection(friendRequestRepository.findAll())
                .stream()
                .filter(friendRequest ->
                        Objects.equals(friendRequest.getTo().getId(), ofId) &&
                                friendRequest.getStatus() == FriendRequestStatus.PENDING)
                .count());
    }

    private FriendRequest updateFriendRequest(Long friendRequestId, FriendRequestStatus newStatus)
            throws NotFoundException, InvalidEntityException {
        FriendRequest friendRequestToUpdate = friendRequestRepository
                .findOne(friendRequestId)
                .orElseThrow(() -> new NotFoundException("Friend request could not be found."));

        if (friendRequestToUpdate.getStatus() != FriendRequestStatus.PENDING) {
            throw new InvalidEntityException("Selected friend request is not pending.");
        }

        friendRequestToUpdate.setStatus(newStatus);

        friendRequestRepository.update(friendRequestToUpdate).ifPresentOrElse(
                u -> {
                    throw new RuntimeException("Error while trying to update friend request.");
                },
                () -> notifyObservers(FriendRequestChangeEvent.ofNewData(ChangeEventType.UPDATE, friendRequestToUpdate)));

        return friendRequestToUpdate;
    }

    public void approveFriendRequest(Long friendRequestId) {
        FriendRequest updatedFriendRequest = updateFriendRequest(friendRequestId, FriendRequestStatus.APPROVED);

        Friendship friendship = new Friendship(
                updatedFriendRequest.getFrom(),
                updatedFriendRequest.getTo(),
                LocalDateTime.now());

        friendshipRepository.save(friendship)
                .ifPresentOrElse(existingFriendship -> {
                    throw new ExistingEntityException("Friendship already exists.");
        }, () -> notifyObservers(FriendshipChangeEvent.ofNewData(ChangeEventType.ADD, friendship)));
    }

    public void rejectFriendRequest(Long friendRequestId) {
        updateFriendRequest(friendRequestId, FriendRequestStatus.REJECTED);
    }

    public void cancelFriendRequest(Long friendRequestId) throws NotFoundException {
        friendRequestRepository.delete(friendRequestId).ifPresentOrElse(
                deletedFriendRequest -> notifyObservers(
                        FriendRequestChangeEvent.ofOldData(ChangeEventType.DELETE, deletedFriendRequest)
                ),
                () -> {
                    throw new NotFoundException("Failed to delete friend request.");
                }
        );
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
