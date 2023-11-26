package ir.map.g221.guisocialnetwork.domain.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Message extends Entity<Long> {
    private User from;
    private Set<User> to;
    private String message;
    private LocalDateTime date;

    public Message(Long aLong, User from, Set<User> toUsers, String message, LocalDateTime date) {
        super(aLong);
        this.from = from;
        this.to = toUsers;
        this.message = message;
        this.date = date;
    }

    public Message(User from, Set<User> toUsers, String message, LocalDateTime date) {
        super(0L);
        this.from = from;
        this.to = toUsers;
        this.message = message;
        this.date = date;
    }

    public Message(Long aLong, User from, String message, LocalDateTime date) {
        super(aLong);
        this.from = from;
        this.to = null;
        this.message = message;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public Set<User> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "From: " + from + " | " +
                " To:\n" + to + "\n| " +
                " Message: '" + message + "' | " +
                " Date: " + date;
    }
}
