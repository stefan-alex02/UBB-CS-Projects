package ir.map.g221.guisocialnetwork.domain.entities;

import java.time.LocalDateTime;

public class FriendRequest extends Entity<Long> {
    private final User from;
    private final User to;
    private FriendRequestStatus status;
    private final LocalDateTime date;

    public FriendRequest(Long aLong, User from, User to, FriendRequestStatus status, LocalDateTime date) {
        super(aLong);
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public FriendRequest(User from, User to, FriendRequestStatus status, LocalDateTime date) {
        super(0L);
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ID : " + id +
                " | From : " + from.getFirstName() + " " + from.getLastName() +
                " | To : " + to.getFirstName() + " " + to.getLastName() +
                " | Status : " + status.name() +
                " | Date : " + date;
    }
}
