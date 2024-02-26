package org.example.modelpractic.business;

import org.example.modelpractic.domain.Order;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.exceptions.NonexistentUsernameException;
import org.example.modelpractic.persistence.Repository;
import org.example.modelpractic.persistence.paging.Page;
import org.example.modelpractic.persistence.paging.Pageable;
import org.example.modelpractic.persistence.paging.PagingRepository;

import java.util.stream.StreamSupport;

public class DriverService {
    private final PagingRepository<Long, Person, Long> personRepository;
    private final Repository<Long, Order> orderRepository;

    public DriverService(PagingRepository<Long, Person, Long> personRepository, Repository<Long, Order> orderRepository) {
        this.personRepository = personRepository;
        this.orderRepository = orderRepository;
    }

    public Page<Person> getClientsOf(Pageable pageable, Long driverId) throws NonexistentUsernameException {
        return personRepository.findAllWhere(pageable, driverId);
    }

    public Long getNumberOfClientsOf(Long driverId) throws NonexistentUsernameException {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(ord -> ord.getTaxiDriver().getID().equals(driverId))
                .map(Order::getPerson)
                .distinct()
                .count();
    }
}
