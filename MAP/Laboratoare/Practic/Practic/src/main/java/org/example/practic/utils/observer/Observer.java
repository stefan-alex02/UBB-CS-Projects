package org.example.practic.utils.observer;

import org.example.practic.utils.events.Event;

public interface Observer<TE extends Event> {
    void update(TE event);
}
