package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.ReplyMessage;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.exceptions.NotFoundException;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.customqueries.ConversationUsersQuery;
import ir.map.g221.guisocialnetwork.persistence.customqueries.SortedMessagesQuery;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.MessageChangeEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.observer.Observable;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MessageService implements Observable {
    private final PagingRepository<Long, Message> messageRepository;
    private final PagingRepository<Long, User> userRepository;
    private final Set<Observer> observers;

    public MessageService(PagingRepository<Long, Message> messageRepository,
                          PagingRepository<Long, User> userRepository) {
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

    public Set<User> getConversationUsers(Long id) {
        Collection<Message> messages = ObjectTransformer.iterableToCollection(messageRepository.findAll());
        Set<User> receiverUsers = messages
                .stream()
                .filter(message -> Objects.equals(message.getFrom().getId(), id))
                .map(Message::getTo)
                .reduce(new HashSet<>(), (conversationUsers, users) -> {
                    conversationUsers.addAll(users);
                    return conversationUsers;
                });
        Set<User> senderUsers = messages
                .stream()
                .filter(message -> message.getTo().stream().anyMatch(receiver ->
                        Objects.equals(receiver.getId(), id)))
                .map(Message::getFrom)
                .collect(Collectors.toSet());
        receiverUsers.addAll(senderUsers);
        return receiverUsers;
    }

    public List<Message> getConversation(Long id1, Long id2) {
        Optional<User> optionalUser1 = userRepository.findOne(id1);
        Optional<User> optionalUser2 = userRepository.findOne(id2);

        if (optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
            throw new NotFoundException("At least one user could not be found.");
        }

        User user1 = optionalUser1.get();
        User user2 = optionalUser2.get();

        return ObjectTransformer.iterableToCollection(messageRepository.findAll())
                .stream()
                .filter(message -> message.belongsToConversation(user1, user2))
                .sorted(Comparator.comparing(Message::getDate))
                .toList();
    }

    public Page<Message> getConversation(Long id1, Long id2, Pageable pageable) {
        Optional<User> optionalUser1 = userRepository.findOne(id1);
        Optional<User> optionalUser2 = userRepository.findOne(id2);

        if (optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
            throw new NotFoundException("At least one user could not be found.");
        }

        return messageRepository.findAllWhere(new SortedMessagesQuery(id1, id2), pageable);
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
            if (!fromUser.hasFriend(optionalReceiver.get())) {
                throw new NotFoundException("Receiver must be friends with sender user.");
            }
            users.add(optionalReceiver.get());
        });

        Message messageToAdd = new Message(fromUser, users, message, LocalDateTime.now());
        if(messageRepository.save(messageToAdd).isEmpty()) {
            notifyObservers(MessageChangeEvent.ofNewData(ChangeEventType.ADD, messageToAdd));
        }
    }

    public void sendReplyMessageNow(Long fromUserId, List<Long> toUsersIds, String message, Long replyToId) {
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
            if (!fromUser.hasFriend(optionalReceiver.get())) {
                throw new NotFoundException("Receiver must be friends with sender user.");
            }
            users.add(optionalReceiver.get());
        });

        Message replyTo = getMessage(replyToId);

        ReplyMessage replyMessageToAdd = new ReplyMessage(fromUser, users, message, LocalDateTime.now(), replyTo);
        if(messageRepository.save(replyMessageToAdd).isEmpty()) {
            notifyObservers(MessageChangeEvent.ofNewData(ChangeEventType.ADD, replyMessageToAdd));
        }
    }

    public void editMessage(Long messageId, String newMessageText) {
        Message message = getMessage(messageId);

        Message updatedMessage = message.copyOf();
        updatedMessage.setMessage(newMessageText);

        if(messageRepository.update(updatedMessage).isEmpty()) {
            notifyObservers(MessageChangeEvent.ofData(
                    ChangeEventType.UPDATE, updatedMessage, message));
        }
    }

    public void removeMessage(Long messageId) {
        messageRepository.delete(messageId).ifPresentOrElse(
                deletedMessage -> notifyObservers(
                        MessageChangeEvent.ofOldData(ChangeEventType.DELETE, deletedMessage)
                ),
                () -> {
                    throw new NotFoundException("Failed to delete message.");
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
