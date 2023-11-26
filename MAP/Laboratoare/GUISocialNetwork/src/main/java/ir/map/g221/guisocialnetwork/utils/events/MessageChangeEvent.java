package ir.map.g221.guisocialnetwork.utils.events;

import ir.map.g221.guisocialnetwork.domain.entities.Message;

public class MessageChangeEvent implements Event {
    private final EventType eventType = EventType.MESSAGE;
    private final ChangeEventType changeEventType;
    private final Message newData;
    private final Message oldData;

    public MessageChangeEvent(ChangeEventType changeEventType, Message newData, Message oldData) {
        this.changeEventType = changeEventType;
        this.newData = newData;
        this.oldData = oldData;
    }

    public static MessageChangeEvent ofNewData(ChangeEventType changeEventType, Message newData) {
        return new MessageChangeEvent(changeEventType, newData, null);
    }

    public static MessageChangeEvent ofOldData(ChangeEventType changeEventType, Message oldData) {
        return new MessageChangeEvent(changeEventType, null, oldData);
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public Message getNewData() {
        return newData;
    }

    public Message getOldData() {
        return oldData;
    }
}
