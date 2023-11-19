package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.User;

public class UserChangeEvent implements Event {
    private final EventType eventType;
    private final User newData;
    private final User oldData;

    public UserChangeEvent(EventType eventType, User newData, User oldData) {
        this.eventType = eventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    public EventType getEventType() {
        return eventType;
    }

    public User getNewData() {
        return newData;
    }

    public User getOldData() {
        return oldData;
    }

    public static UserChangeEvent ofNewData(EventType eventType, User newData) {
        return new UserChangeEvent(eventType, newData, null);
    }

    public static UserChangeEvent ofOldData(EventType eventType, User oldData) {
        return new UserChangeEvent(eventType, null, oldData);
    }
}
