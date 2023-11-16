package ir.map.g221.guisocialnetwork.utils.observer;

import ir.map.g221.guisocialnetwork.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
