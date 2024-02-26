package org.example.zboruri.utils.events;

import org.example.zboruri.domain.Ticket;

public class TicketEvent implements Event {
    private final TicketEventType ticketEventType;
    private final Ticket data;

    public TicketEvent(TicketEventType ticketEventType, Ticket data) {
        this.ticketEventType = ticketEventType;
        this.data = data;
    }

    public TicketEventType getTicketEventType() {
        return ticketEventType;
    }

    public Ticket getData() {
        return data;
    }
}
