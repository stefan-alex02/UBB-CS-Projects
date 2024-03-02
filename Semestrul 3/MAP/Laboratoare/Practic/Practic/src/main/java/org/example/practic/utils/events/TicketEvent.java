package org.example.practic.utils.events;

import org.example.practic.domain.DummyEntity;

public class TicketEvent implements Event {
    private final DummyEntityEventType dummyEntityEventType;
    private final DummyEntity data;

    public TicketEvent(DummyEntityEventType dummyEntityEventType, DummyEntity data) {
        this.dummyEntityEventType = dummyEntityEventType;
        this.data = data;
    }

    public DummyEntityEventType getDummyEntityEventType() {
        return dummyEntityEventType;
    }

    public DummyEntity getData() {
        return data;
    }
}
