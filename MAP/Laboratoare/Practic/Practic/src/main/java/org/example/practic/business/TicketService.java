package org.example.practic.business;

import javafx.util.Pair;
import org.example.practic.domain.Ticket;
import org.example.practic.persistence.Repository;
import org.example.practic.utils.events.TicketEvent;
import org.example.practic.utils.observer.Observable;
import org.example.practic.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class TicketService implements Observable<TicketEvent> {
    private final Repository<Long, Ticket> ticketRepository;
    private final List<Observer<TicketEvent>> observerList;

    public TicketService(Repository<Long, Ticket> ticketRepository) {
        this.ticketRepository = ticketRepository;
        observerList = new ArrayList<>();
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsOf(String cityId, String train) {
        return StreamSupport.stream(ticketRepository.findAll().spliterator(), false)
                .filter(t -> t.getTrainId().equals(train) &&
                        t.getDepartureCityId().equals(cityId))
                .toList();
    }

    @Override
    public void addObserver(Observer<TicketEvent> observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer<TicketEvent> observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(TicketEvent event) {
        observerList.forEach(observer -> observer.update(event));
    }
}
