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
public class StateMachineImpl<S, E> implements StateMachine<S, E> {

    private final String machineId;

    private final Collection<State<S>> states;

    private final State<S> initial;

    private final State<S> end;

    private final Collection<Event<E>> events;

    private final Transition<S, E> initialTransition;

    private final Collection<Transition<S, E>> transitions;

    private StateMachinePersist<S, E> stateMachinePersist = new InMemoryStateMachinePersist<>();

    private StateMachineLock<S> stateMachineLock = new DefaultStateMachineLock<>();

    public StateMachineImpl(String machineId,
                            Collection<State<S>> states,
                            State<S> initial,
                            State<S> end,
                            Collection<Event<E>> events,
                            Transition<S, E> initialTransition,
                            Collection<Transition<S, E>> transitions,
                            StateMachinePersist<S, E> stateMachinePersist,
                            StateMachineLock<S> stateMachineLock) {
        Assert.hasText(machineId, "id must not be blank");
        Assert.notNull(initial, "initial must not be null");
        Assert.notNull(end, "end must not be null");
        Assert.notEmpty(states, "state must not be empty");
        Assert.notEmpty(events, "events must not be empty");
        Assert.notEmpty(transitions, "transitions must not be empty");
        this.machineId = machineId;
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

    @Override
    public String getMachineId() {
        return machineId;
    }

    @Override
    public Collection<Event<E>> getEvents() {
        return events;
    }

    @Override
    public Collection<State<S>> getStates() {
        return states;
    }

    @Override
    public State<S> getInitial() {
        return initial;
    }

    public <P> void sendEvent(String bizId, E event, P params) {
        Assert.hasText(bizId, "bizId must not be null");
        Assert.notNull(event, "event must not be null");

        // 取出业务数据
        StateData stateData = stateMachinePersist.read(this.machineId, bizId);

        if (stateData == null) {
            throw new StateMachineException("State object has not been initialized");
        }

        if (stateData.getState().equals(end.toString())) {
            throw new StateMachineException("State object has been ended");
        }

        // 锁定当前状态数据
        if (!stateMachineLock.lock(stateData)) {
            throw new StateMachineException("State object is being processed");
        }

        try {
            String currentState = stateData.getState();

            Transition<S, E> transition = findTransition(new Event<>(event), currentState);
            if (transition == null) {
                throw new StateMachineException("Not found transition: source=[%s], event=[%s]".formatted(currentState, event));
            }

            StateContext<S, E> stateContext = buildContext(stateData.getBizId(), params, transition);

            if (!transition.executeGuards(stateContext)) {
                return;
            }

            transition.executeActions(stateContext);

            // do persist
            stateMachinePersist.write(buildStateData(stateData.getId(), stateContext), stateContext);

        } finally {
            unlock(stateData);
        }

    }

    @Override
    public <P> S nextState(S source, E event, P params) {
        Assert.notNull(source, "source must not be null");
        Assert.notNull(source, "event must not be null");

        if (new State<>(source).getValue().toString().equals(end.toString())) {
            throw new StateMachineException("State object has been ended");
        }

        String currentState = source.toString();
        Transition<S, E> transition = findTransition(new Event<>(event), currentState);
        if (transition == null) {
            throw new StateMachineException("Not found transition: source=[%s], event=[%s]".formatted(currentState, event));
        }

        StateContext<S, E> stateContext = buildContext(null, params, transition);

        if (!transition.executeGuards(stateContext)) {
            throw new StateMachineException("Execute guards not passed");
        }

        transition.executeActions(stateContext);
        return transition.getTarget().getValue();
    }

    private void unlock(StateData stateData) {
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

    private StateData buildStateData(Long stateDataId, StateContext<S, E> stateContext) {
        boolean ended = stateContext.getTransition().getTarget().toString().equals(end.toString());
        StateData data = new StateData();
        data.setId(stateDataId);
        data.setMachineId(stateContext.getMachineId());
        data.setBizId(stateContext.getBizId());
        data.setState(stateContext.getTransition().getTarget().toString());
        data.setEnded(ended);
        data.setExtras(Maps.newHashMap());
        return data;
    }

    public <P> void init(String bizId, E event, P params) {

        // 取出业务数据
        StateData stateData = stateMachinePersist.read(this.machineId, bizId);

        Assert.isNull(stateData, "State object has been initialized");

        StateContext<S, E> ctx = buildContext(bizId, params, this.initialTransition);

        if (!initialTransition.executeGuards(ctx)) {
            return;
        }

        initialTransition.executeActions(ctx);

        stateMachinePersist.write(buildStateData(null, ctx), ctx);

    }

    private <P> StateContext<S, E> buildContext(String bizId, P params, Transition<S, E> transition) {
        StateContext<S, E> stateContext = new StateContext<>();
        stateContext.setTransition(transition);
        stateContext.setMachineId(this.machineId);
        stateContext.setBizId(bizId);
        stateContext.setParams(params);
        stateContext.setExtras(Maps.newHashMap());
        return stateContext;
    }

    private Transition<S, E> findTransition(Event<E> currentEvent, String currentState) throws StateMachineException {
        if (transitions == null || transitions.size() == 0) {
            return null;
        }
        for (Transition<S, E> transition : transitions) {
            if (currentState.equals(transition.getSource().toString())
                    && transition.getEvent().getValue().equals(currentEvent.getValue())) {
                log.info("Found transition: source=[{}] -> event=[{}] -> target=[{}]",
                        transition.getSource(), transition.getEvent(), transition.getTarget());
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
