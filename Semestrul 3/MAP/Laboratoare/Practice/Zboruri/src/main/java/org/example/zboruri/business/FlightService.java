package org.example.zboruri.business;

import javafx.util.Pair;
import org.example.zboruri.domain.FlightDTO;
import org.example.zboruri.domain.Ticket;
import org.example.zboruri.persistence.Repository;
import org.example.zboruri.persistence.paging.Page;
import org.example.zboruri.persistence.paging.PageImplementation;
import org.example.zboruri.persistence.paging.Pageable;
import org.example.zboruri.persistence.pagingrepos.FlightsDBPagingRepository;

import java.time.LocalDate;
import java.util.stream.StreamSupport;

public class FlightService {
    private final FlightsDBPagingRepository flightRepository;
    private final Repository<Pair<String, Long>, Ticket> ticketRepository;

    public FlightService(FlightsDBPagingRepository flightRepository,
                         Repository<Pair<String, Long>, Ticket> ticketRepository) {
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    public Page<FlightDTO> getFlightsOf(Pageable pageable, LocalDate date, String from, String to) {
        return new PageImplementation<>(pageable,
                flightRepository
                .findAllWhere(pageable, date, from, to)
                .getContent()
                .map(f -> FlightDTO.ofFlight(f,
                        (int) StreamSupport.stream(
                                ticketRepository.findAll().spliterator(), false)
                                .filter(t -> t.getID().getValue().equals(f.getID()))
                                .count()
                )));
    }

    public Integer getNoOfFlightsOf(LocalDate date, String from, String to) {
        return (int) StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                .filter(f -> f.getDepartureTime().toLocalDate().equals(date) &&
                        f.getFrom().equals(from) &&
                        f.getTo().equals(to))
                .count();
    }
}
