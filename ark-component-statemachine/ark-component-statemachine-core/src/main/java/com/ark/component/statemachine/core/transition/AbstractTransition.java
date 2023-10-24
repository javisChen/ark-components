/*
 * Copyright 2015-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KINDither express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ark.component.statemachine.core.transition;

import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.StateMachineException;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.trigger.Trigger;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

@Slf4j
public abstract class AbstractTransition<S, E, T> implements Transition<S, E, T> {

    protected final State<S> target;
    protected final Collection<Action<E>> actions;
    private final State<S> source;
    private final TransitionKind kind;
    private final List<Guard<E>> guards;
    private final Trigger<S, E> trigger;
    private final String name;

    protected AbstractTransition(State<S> source,
                                 State<S> target,
                                 TransitionKind kind,
                                 List<Guard<E>> guards,
                                 Collection<Action<E>> actions,
                                 Trigger<S, E> trigger,
                                 String name) {
        this.target = target;
        this.actions = actions;
        this.source = source;
        this.kind = kind;
        this.guards = guards;
        this.trigger = trigger;
        this.name = name;
    }


    @Override
    public State<S> getSource() {
        return source;
    }

    @Override
    public Trigger<S, E> getTrigger() {
        return trigger;
    }

    @Override
    public <P> boolean executeGuards(StateContext<E> context) {
        if (guards == null) {
            return true;
        }
        try {
            for (Guard<E> guard : guards) {
                if (!guard.evaluate(context)) {
                    log.warn("Guard [{}] returned false", guard);
                    return false;
                }
            }
        } catch (Throwable t) {
            log.error("Deny guard due to throw as GUARD should not error", t);
            return false;
        }
        return true;
    }

    @Override
    public State<S> getTarget() {
        return target;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <P> void executeActions(StateContext<E> context) {
        if (actions == null) {
            return;
        }
        try {
            for (Action<E> action : actions) {
                action.execute(context);
            }
        } catch (Exception e) {
            log.warn("Aborting as transition " + this, e);
            throw new StateMachineException("Aborting as transition " + this + " caused error " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "AbstractTransition [source=" + source + ", target=" + target + ", kind=" + kind + ", guard=" + guards + "]";
    }
}
