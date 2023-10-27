package com.ark.component.statemachine.core.trigger;

import com.ark.component.statemachine.core.Event;

public class EventTrigger<E> implements Trigger<E> {

    private final Event<E> event;

    public EventTrigger(Event<E> event) {
        this.event = event;
    }

    @Override
    public Event<E> getEvent() {
        return this.event;
    }

    @Override
    public String toString() {
        return event.toString();
    }
}
