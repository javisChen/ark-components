package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.transition.Transition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StateMachineTransitionBuilder<S, E> implements Builder<S, E> {

    private final Collection<ExternalTransitionBuilder<S, E>> transitions = new ArrayList<>();

    public ExternalTransitionBuilder<S, E> withExternal() {
        ExternalTransitionBuilder<S, E> externalTransitionBuilder = new ExternalTransitionBuilder<>(this);
        transitions.add(externalTransitionBuilder);
        return externalTransitionBuilder;
    }

    public List<Transition<S, E>> build() {
        return this.transitions.stream().map(ExternalTransitionBuilder::build).toList();
    }

}

