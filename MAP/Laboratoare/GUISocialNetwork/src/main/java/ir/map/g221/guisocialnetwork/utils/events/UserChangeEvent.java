package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.User;

public class UserChangeEvent implements Event {
    private ChangeEventType eventType;
    private User newData, oldData;

    public UserChangeEvent(ChangeEventType eventType, User newData, User oldData) {
        this.eventType = eventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    public static UserChangeEvent ofNewData(ChangeEventType eventType, User newData) {
        return new UserChangeEvent(eventType, newData, null);
    }

    public static UserChangeEvent ofOldData(ChangeEventType eventType, User oldData) {
        return new UserChangeEvent(eventType, null, oldData);
    }
}
