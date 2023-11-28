package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.MessageChangeEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.observer.Observable;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageService implements Observable {
    private final Repository<Long, Message> messageRepository;
    private final Repository<Long, User> userRepository;
    private final Set<Observer> observers;

    public MessageService(Repository<Long, Message> messageRepository, Repository<Long, User> userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        observers = Collections.newSetFromMap(new ConcurrentHashMap<>(0));
    }

    public Message getMessage(Long id) {
        return messageRepository
                .findOne(id)
                .orElseThrow(() ->
                        new NotFoundException("Message could not be found.")
                );
    }

    public List<Message> getConversation(Long id1, Long id2) {
        if (userRepository.findOne(id1).isEmpty() || userRepository.findOne(id2).isEmpty()) {
            throw new NotFoundException("At least one user could not be found.");
        }

        return ObjectTransformer.iterableToCollection(messageRepository.findAll())
                .stream()
                .filter(message ->
                        (Objects.equals(message.getFrom().getId(), id1) &&
                            message.getTo().stream().anyMatch(receiver ->
                                    Objects.equals(receiver.getId(), id2))) ||
                        (Objects.equals(message.getFrom().getId(), id2) &&
                            message.getTo().stream().anyMatch(receiver ->
                                    Objects.equals(receiver.getId(), id1))))
                .sorted(Comparator.comparing(Message::getDate))
                .toList();
    }

    public void sendMessageNow(Long fromUserId, List<Long> toUsersIds, String message) {
        Optional<User> optionalUser = userRepository.findOne(fromUserId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Sender user could not be found.");
        }
        User fromUser = optionalUser.get();

        Set<User> users = new HashSet<>();
        toUsersIds.forEach(id -> {
            Optional<User> optionalReceiver = userRepository.findOne(id);
            if (optionalReceiver.isEmpty()) {
                throw new NotFoundException("A receiver user could not be found.");
            }
            users.add(optionalReceiver.get());
        });

        Message messageToAdd = new Message(fromUser, users, message, LocalDateTime.now());
        if(messageRepository.save(messageToAdd).isEmpty()) {
            notifyObservers(MessageChangeEvent.ofNewData(ChangeEventType.ADD, messageToAdd));
        }
    }

    public void sendReplyMessageNow(Long fromUserId, List<Long> toUsersIds, String message, Message messageRepliedTo) {

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
