package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;

public class FriendshipChangeEvent implements Event {
    private final EventType eventType = EventType.FRIENDSHIP;
    private final ChangeEventType changeEventType;
    private final Friendship newData;
    private final Friendship oldData;

    public FriendshipChangeEvent(ChangeEventType changeEventType, Friendship newData, Friendship oldData) {
        this.changeEventType = changeEventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public Friendship getNewData() {
        return newData;
    }

    public Friendship getOldData() {
        return oldData;
    }

    public static FriendshipChangeEvent ofNewData(ChangeEventType changeEventType, Friendship newData) {
        return new FriendshipChangeEvent(changeEventType, newData, null);
    }

    public static FriendshipChangeEvent ofOldData(ChangeEventType changeEventType, Friendship oldData) {
        return new FriendshipChangeEvent(changeEventType, null, oldData);
    }
}
