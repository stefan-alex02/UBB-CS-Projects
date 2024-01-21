package org.example.modelpractic.business;

import org.example.modelpractic.domain.Order;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.domain.PlacedOrder;
import org.example.modelpractic.persistence.Repository;
import org.example.modelpractic.utils.events.OrderEvent;
import org.example.modelpractic.utils.observer.Observable;
import org.example.modelpractic.utils.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderService implements Observable<OrderEvent> {
    private Repository<Long, Order> orderRepository;
    private List<Observer<OrderEvent>> observers;

    public OrderService(Repository<Long, Order> orderRepository) {
        this.orderRepository = orderRepository;
        observers = new ArrayList<>();
    }

    public void addOrder(PlacedOrder placedOrder) {
        orderRepository.save(new Order(
                0L,
                placedOrder.getPerson(),
                placedOrder.getDriver(),
                LocalDateTime.now()
        ));
    }

    public List<Order> getOrdersOf(LocalDate date, Long driverId) {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(ord -> ord.getDate().toLocalDate().isEqual(date) &&
                                ord.getTaxiDriver().getID().equals(driverId))
                .toList();
    }

    public Double last3MonthsOrderAverage(Long driverId) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);

        List<Order> filteredOrders = StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getDate().isAfter(threeMonthsAgo) &&
                        order.getTaxiDriver().getID().equals(driverId))
                .toList();

        // Group orders by date and count the number of orders for each day
        Map<LocalDateTime, Long> ordersPerDay = filteredOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getDate().toLocalDate().atStartOfDay(), Collectors.counting()));

        // Calculate the average number of orders per day
        return ordersPerDay.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }

    public Optional<Person> celMaiFidelClient(Long driverId) {
        List<Order> filteredOrders = StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getTaxiDriver().getID().equals(driverId))
                .toList();

        Map<Person, Long> ordersPerDay = filteredOrders.stream()
                .collect(Collectors.groupingBy(Order::getPerson, Collectors.counting()));

        return ordersPerDay.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    @Override
    public void addObserver(Observer<OrderEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<OrderEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(OrderEvent event) {
        observers.forEach(obs -> obs.update(event));
    }
}
