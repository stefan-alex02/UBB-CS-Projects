package ir.map.g221.guisocialnetwork.domain.entities;

import java.time.LocalDateTime;

public class FriendRequest extends Entity<Long> {
    private final User from;
    private final User to;
    private final LocalDateTime date;

    protected FriendRequest(Long aLong, User from, User to, LocalDateTime date) {
        super(aLong);
        this.from = from;
        this.to = to;
        this.date = date;
    }

    protected FriendRequest(User from, User to, LocalDateTime date) {
        super(0L);
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
