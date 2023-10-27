package com.ark.component.statemachine.core;

import com.ark.component.statemachine.core.transition.Transition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
public class StateContext<S, E> {

    private String machineId;

    private String bizId;

    private Object params;

    private Transition<S, E> transition;

    private Map<String, Object> extras;

}
