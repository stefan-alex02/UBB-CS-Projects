package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;

public class FriendRequestChangeEvent implements Event {
    private final ChangeEventType changeEventType;
    private final FriendRequest newData;
    private final FriendRequest oldData;

    public FriendRequestChangeEvent(ChangeEventType changeEventType, FriendRequest newData, FriendRequest oldData) {
        this.changeEventType = changeEventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    public static FriendRequestChangeEvent ofNewData(ChangeEventType changeEventType, FriendRequest newData) {
        return new FriendRequestChangeEvent(changeEventType, newData, null);
    }

    public static FriendRequestChangeEvent ofOldData(ChangeEventType changeEventType, FriendRequest oldData) {
        return new FriendRequestChangeEvent(changeEventType, null, oldData);
    }

    @Override
    public EventType getEventType() {
        return EventType.FRIEND_REQUEST;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public FriendRequest getNewData() {
        return newData;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}
