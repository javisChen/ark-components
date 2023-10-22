package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.List;

@Data
public class StateMachine<S, E> {

    private String id;

    private List<State<S>> states;

    private List<Event<E>> events;

    private List<Transition<S, E>> transitions;


}
