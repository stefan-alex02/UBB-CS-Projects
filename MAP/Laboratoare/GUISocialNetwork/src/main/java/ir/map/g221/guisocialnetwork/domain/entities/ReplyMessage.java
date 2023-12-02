package ir.map.g221.guisocialnetwork.domain.entities;

import java.time.LocalDateTime;
import java.util.Set;

public class ReplyMessage extends Message {
    private final Message messageRepliedTo;

    public ReplyMessage(Long aLong, User from, Set<User> toUsers, String message, LocalDateTime date,
                           Message messageRepliedTo) {
        super(aLong, from, toUsers, message, date);
        this.messageRepliedTo = messageRepliedTo;
    }

    public ReplyMessage(User from, Set<User> toUsers, String message, LocalDateTime date,
                        Message messageRepliedTo) {
        super(0L, from, toUsers, message, date);
        this.messageRepliedTo = messageRepliedTo;
    }

    @Override
    public Message copyOf() {
        return new ReplyMessage(id, from,
                to, message,
                date, messageRepliedTo);
    }

    public Message getMessageRepliedTo() {
        return messageRepliedTo;
    }

    @Override
    public String toString() {
        return super.toString() + " | " +
                " Replied to: " + messageRepliedTo.id + " , " +
                messageRepliedTo.getFrom() + " , " +
                getMessageRepliedTo().getMessage();
    }
}
