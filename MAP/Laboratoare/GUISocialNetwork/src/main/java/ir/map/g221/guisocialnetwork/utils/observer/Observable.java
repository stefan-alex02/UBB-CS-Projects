package ir.map.g221.guisocialnetwork.utils.observer;

import ir.map.g221.guisocialnetwork.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyObservers(E event);
}
