package org.example.zboruri.utils.observer;

import org.example.zboruri.utils.events.Event;

public interface Observer<TE extends Event> {
    void update(TE event);
}
