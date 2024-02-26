package org.example.zboruri.business;

import javafx.util.Pair;
import org.example.zboruri.domain.Ticket;
import org.example.zboruri.persistence.Repository;
import org.example.zboruri.utils.events.TicketEvent;
import org.example.zboruri.utils.events.TicketEventType;
import org.example.zboruri.utils.observer.Observable;
import org.example.zboruri.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketService implements Observable<TicketEvent> {
    private final Repository<Pair<String, Long>, Ticket> ticketRepository;
    private final List<Observer<TicketEvent>> observers;

    public TicketService(Repository<Pair<String, Long>, Ticket> ticketRepository) {
        this.ticketRepository = ticketRepository;
        observers = new ArrayList<>();
    }

    public void addTicket(String username, Long flightId) {
        Ticket ticket = new Ticket(new Pair<>(username, flightId), LocalDateTime.now());
        ticketRepository.save(ticket);

        notifyObservers(
                new TicketEvent(TicketEventType.PURCHASED, ticket));
    }

    @Override
    public void addObserver(Observer<TicketEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<TicketEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(TicketEvent event) {
        observers.forEach(obs -> obs.update(event));
    }
}
