package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.User;

public class UserChangeEvent implements Event {
    private final EventType eventType = EventType.USER;
    private final ChangeEventType changeEventType;
    private final User newData;
    private final User oldData;

    public UserChangeEvent(ChangeEventType changeEventType, User newData, User oldData) {
        this.changeEventType = changeEventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    public static UserChangeEvent of(ChangeEventType changeEventType, User newData, User oldData) {
        return new UserChangeEvent(changeEventType, newData, oldData);
    }

    public static UserChangeEvent ofNewData(ChangeEventType changeEventType, User newData) {
        return new UserChangeEvent(changeEventType, newData, null);
    }

    public static UserChangeEvent ofOldData(ChangeEventType changeEventType, User oldData) {
        return new UserChangeEvent(changeEventType, null, oldData);
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public User getNewData() {
        return newData;
    }

    public User getOldData() {
        return oldData;
    }
}
