package com.ark.component.statemachine.core;

import lombok.Data;

@Data
public class Event<E> {

    private E id;

    public Event(E id) {
        this.id = id;
    }

}
