package com.ark.component.statemachine.core;

import lombok.Data;

@Data
public class Event<E> {

    private E value;

    public Event(E value) {
        this.value = value;
    }

}
