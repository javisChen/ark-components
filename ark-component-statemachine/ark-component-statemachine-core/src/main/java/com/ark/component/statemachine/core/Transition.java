package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.List;

@Data
public class Transition<S, E> {

    private List<String> guards;

    private List<S> source;

    private E event;

    private S target;

    private List<String> actions;

}
