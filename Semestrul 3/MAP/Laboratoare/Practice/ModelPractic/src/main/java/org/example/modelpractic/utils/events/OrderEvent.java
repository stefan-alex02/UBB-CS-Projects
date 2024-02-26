package org.example.modelpractic.utils.events;

import org.example.modelpractic.domain.PlacedOrder;

public class OrderEvent implements Event {
    private final OrderEventType orderEventType;
    private final PlacedOrder data;

    public OrderEvent(OrderEventType orderEventType, PlacedOrder data) {
        this.orderEventType = orderEventType;
        this.data = data;
    }

    public OrderEventType getOrderEventType() {
        return orderEventType;
    }

    public PlacedOrder getData() {
        return data;
    }
}
