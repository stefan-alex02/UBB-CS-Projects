package org.example.practic.utils.observer;

import org.example.practic.utils.events.Event;

public interface Observable<TE extends Event> {
    void addObserver(Observer<TE> observer);
    void removeObserver(Observer<TE> observer);
    void notifyObservers(TE event);
}
