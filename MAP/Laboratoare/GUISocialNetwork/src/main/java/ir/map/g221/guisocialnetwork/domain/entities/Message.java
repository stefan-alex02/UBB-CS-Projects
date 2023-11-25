package ir.map.g221.guisocialnetwork.domain.entities;

import java.time.LocalDateTime;
import java.util.Set;

public class Message extends Entity<Long> {
    private User from;
    private Set<User> to;
    private String message;
    private LocalDateTime date;

    protected Message(Long aLong, User from, Set<User> toUsers, String message, LocalDateTime date) {
        super(aLong);
        this.from = from;
        to = toUsers;
        this.message = message;
        this.date = date;
    }

}
