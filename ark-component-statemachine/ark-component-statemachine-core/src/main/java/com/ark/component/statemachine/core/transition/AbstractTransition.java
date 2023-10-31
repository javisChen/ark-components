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

import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.StateMachineException;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Slf4j
public abstract class AbstractTransition<S, E> implements Transition<S, E> {

    protected final State<S> target;
    protected final Collection<Action<S, E>> actions;
    private final State<S> source;
    private final TransitionKind kind;
    private final Collection<Guard<S, E>> guards;
    private final Event<E> event;
    private final String name;

    protected AbstractTransition(State<S> source,
                                 State<S> target,
                                 TransitionKind kind,
                                 Collection<Guard<S, E>> guards,
                                 Collection<Action<S, E>> actions,
                                 Event<E> event,
                                 String name) {
        if (kind != TransitionKind.INITIAL) {
            Assert.notNull(source, "source must not be null");
            Assert.notNull(event, "trigger must not be null");
        }
        Assert.notNull(target, "target must not be null");
        this.target = target;
        this.actions = actions;
        this.source = source;
        this.kind = kind;
        this.guards = guards;
        this.event = event;
        this.name = StringUtils.hasText(name) ? name : "";
    }


    @Override
    public State<S> getSource() {
        return source;
    }

    @Override
    public boolean executeGuards(StateContext<S, E> context) {
        if (guards == null) {
            return true;
        }
        try {
            for (Guard<S, E> guard : guards) {
                if (!guard.evaluate(context)) {
                    log.warn("Guard [{}] returned false", guard);
                    return false;
                }
            }
        } catch (Throwable t) {
            log.error("Deny guard due to throw as GUARD should not error", t);
            throw new StateMachineException(t.getMessage());
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
    public Event<E> getEvent() {
        return this.event;
    }

    @Override
    public TransitionKind getKind() {
        return this.kind;
    }

    @Override
    public void executeActions(StateContext<S, E> context) {
        if (actions == null) {
            return;
        }
        try {
            for (Action<S, E> action : actions) {
                action.execute(context);
            }
        } catch (Exception e) {
            log.warn("Aborting as transition " + this, e);
            throw new StateMachineException("Aborting as transition " + this + " caused error " + e.getMessage());
        }
    }

}
