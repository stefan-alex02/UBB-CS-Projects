package org.example.modelpractic.utils.observer;

import org.example.modelpractic.utils.events.Event;

public interface Observer<TE extends Event> {
    void update(TE event);
}
