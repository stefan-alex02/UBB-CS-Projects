package ir.map.g221.guisocialnetwork.domain.entities;

import java.time.LocalDateTime;
import java.util.Set;

public class ReplyMessage extends Message {
    private final Message message;
    protected ReplyMessage(Long aLong, User from, Set<User> toUsers, String message, LocalDateTime date, Message message1) {
        super(aLong, from, toUsers, message, date);
        this.message = message1;
    }


}
