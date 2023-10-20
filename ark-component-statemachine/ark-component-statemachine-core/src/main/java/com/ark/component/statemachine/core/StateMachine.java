package com.ark.component.statemachine.core;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class StateMachine<S, E> {

    private String id;
    private List<S> states;
    private List<E> events;
    private List<String> finalState;
    private List<Transition<S, E>> transitions;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
