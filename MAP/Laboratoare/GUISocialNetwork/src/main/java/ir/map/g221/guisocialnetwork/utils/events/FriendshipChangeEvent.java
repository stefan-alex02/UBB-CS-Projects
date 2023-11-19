package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;

public class FriendshipChangeEvent implements Event {
    private final EventType eventType;
    private final Friendship newData;
    private final Friendship oldData;

    public FriendshipChangeEvent(EventType eventType, Friendship newData, Friendship oldData) {
        this.eventType = eventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Friendship getNewData() {
        return newData;
    }

    public Friendship getOldData() {
        return oldData;
    }

    public static FriendshipChangeEvent ofNewData(EventType eventType, Friendship newData) {
        return new FriendshipChangeEvent(eventType, newData, null);
    }

    public static FriendshipChangeEvent ofOldData(EventType eventType, Friendship oldData) {
        return new FriendshipChangeEvent(eventType, null, oldData);
    }
}
