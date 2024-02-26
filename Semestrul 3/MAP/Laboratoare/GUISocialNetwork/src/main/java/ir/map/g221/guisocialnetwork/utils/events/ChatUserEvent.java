package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.User;

public class ChatUserEvent implements Event {
    private final ChangeEventType changeEventType;
    private final User userData;

    private ChatUserEvent(ChangeEventType changeEventType, User userData) {
        this.changeEventType = changeEventType;
        this.userData = userData;
    }

    public static ChatUserEvent ofData(ChangeEventType changeEventType, User data) {
        return new ChatUserEvent(changeEventType, data);
    }

    @Override
    public EventType getEventType() {
        return EventType.CHAT_USER;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public User getUserData() {
        return userData;
    }

}
