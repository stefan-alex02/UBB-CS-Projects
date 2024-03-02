package ir.map.g221.guisocialnetwork.utils.events;

public class OpenedEvent implements Event {
    @Override
    public EventType getEventType() {
        return EventType.OPENED;
    }
}
