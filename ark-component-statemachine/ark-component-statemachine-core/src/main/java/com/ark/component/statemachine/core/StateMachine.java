package com.ark.component.statemachine.core;

import com.alibaba.fastjson2.JSON;
import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
import com.ark.component.statemachine.core.lock.StateMachineLock;
import com.ark.component.statemachine.core.persist.InMemoryStateMachinePersist;
import com.ark.component.statemachine.core.persist.StateMachinePersist;
import com.ark.component.statemachine.core.transition.Transition;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.util.Collection;

@Slf4j
public class StateMachine<S, E> {

    private final String id;

    private final Collection<State<S>> states;

    private final State<S> initial;

    private final State<S> end;

    private final Collection<Event<E>> events;

    private final Transition<S, E> initialTransition;

    private final Collection<Transition<S, E>> transitions;

    private StateMachinePersist<S> stateMachinePersist = new InMemoryStateMachinePersist<>();

    private StateMachineLock<S> stateMachineLock = new DefaultStateMachineLock<>();

    public StateMachine(String id,
                        Collection<State<S>> states,
                        State<S> initial,
                        State<S> end,
                        Collection<Event<E>> events,
                        Transition<S, E> initialTransition,
                        Collection<Transition<S, E>> transitions,
                        StateMachinePersist<S> stateMachinePersist,
                        StateMachineLock<S> stateMachineLock) {
        Assert.hasText(id, "id must not be blank");
        Assert.notNull(initial, "initial must not be null");
        Assert.notNull(end, "end must not be null");
        Assert.notEmpty(states, "state must not be empty");
        Assert.notEmpty(events, "events must not be empty");
        Assert.notEmpty(transitions, "transitions must not be empty");
        this.id = id;
        this.states = states;
        this.initial = initial;
        this.end = end;
        this.events = events;
        this.initialTransition = initialTransition;
        this.transitions = transitions;
        if (stateMachinePersist != null) {
            this.stateMachinePersist = stateMachinePersist;
        }
        if (stateMachineLock != null) {
            this.stateMachineLock = stateMachineLock;
        }
    }

    public String getId() {
        return id;
    }

    public Collection<Event<E>> getEvents() {
        return events;
    }

    public Collection<State<S>> getStates() {
        return states;
    }

    public <P> void sendEvent(String id, E event, P params) {

        // 取出业务数据
        StateData<S> stateData = stateMachinePersist.read(id);

        if (stateData == null) {
            throw new StateMachineException("State object has not been initialized");
        }

        if (stateData.getState().equals(end)) {
            throw new StateMachineException("State object has been ended");
        }

        // 锁定当前状态数据
        if (!stateMachineLock.lock(stateData)) {
            throw new StateMachineException("State object is being processed");
        }

        try {
            State<S> currentState = stateData.getState();

            Transition<S, E> transition = findTransition(new Event<>(event), currentState);
            if (transition == null) {
                throw new StateMachineException(String.format("Cannot find transition, Event = %s, State = %s", event, currentState));
            }

            StateContext<E> stateContext = buildContext(stateData, params, event);

            if (!transition.executeGuards(stateContext)) {
                return;
            }

            transition.executeActions(stateContext);

            State<S> target = transition.getTarget();
            stateData.setState(target);
            stateData.setEnded(target.equals(end));

            // write
            stateMachinePersist.write(stateData);

        } finally {
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        stateMachineLock.unlock(stateData);
                    }
                });
            } else {
                stateMachineLock.unlock(stateData);
            }
        }

    }

    public <P> void init(String id, E event, P params) {

        // 取出业务数据
        StateData<S> stateData = stateMachinePersist.read(id);

        if (stateData != null) {
            throw new StateMachineException("State object has been initialized");
        }

        stateData = new StateData<>();
        stateData.setMachineId(this.id);
        stateData.setBizId(id);
        stateData.setState(initial);
        stateData.setExtras(Maps.newHashMap());
        StateContext<E> ctx = buildContext(stateData, params, event);

        if (!initialTransition.executeGuards(ctx)) {
            return;
        }

        initialTransition.executeActions(ctx);

        stateMachinePersist.write(stateData);

    }

    private <P> StateContext<E> buildContext(StateData<S> stateData, P params, E event) {
        StateContext<E> stateContext = new StateContext<>();
        stateContext.setBizCode(stateData.getMachineId());
        stateContext.setBizId(stateData.getBizId());
        stateContext.setEvent(new Event<>(event));
        stateContext.setParams(params);
        stateContext.setExtras(Maps.newHashMap());
        return stateContext;
    }

    private Transition<S, E>  findTransition(Event<E> event, State<S> currentState) {
        if (transitions == null || transitions.size() == 0) {
            return null;
        }
        for (Transition<S, E> transition : transitions) {
            if (currentState.getId().equals(transition.getSource().getId())
                    && transition.getTrigger().getEvent().getId().equals(event.getId())) {
                return transition;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
