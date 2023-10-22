package com.ark.component.statemachine.core;

import lombok.Data;

@Data
public class Transition<S, E> {

    private State<S> source;
    private State<S> target;
    private Event<E> event;

}
