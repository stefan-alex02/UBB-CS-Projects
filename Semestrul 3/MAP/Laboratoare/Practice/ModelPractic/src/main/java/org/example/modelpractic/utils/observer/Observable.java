package org.example.modelpractic.utils.observer;

import org.example.modelpractic.utils.events.Event;

public interface Observable<TE extends Event> {
    void addObserver(Observer<TE> observer);
    void removeObserver(Observer<TE> observer);
    void notifyObservers(TE event);
}
