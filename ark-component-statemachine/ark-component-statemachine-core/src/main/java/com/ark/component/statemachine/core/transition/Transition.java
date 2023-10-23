package com.ark.component.statemachine.core.transition;

import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateMachineContext;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.Collection;

/**
 * {@code Transition} is something what a state machine associates with a state
 * changes.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface Transition<S, E, T> {

	/**
	 * Transit this transition with a give state context.
	 *
	 * @param context the state context
	 * @return true, if transition happened, false otherwise
	 */
	boolean executeGuards(StateMachineContext<S, E, T> context);

	/**
	 * Execute transition actions.
	 *
	 * @param context the state context
	 */
	void executeActions(StateMachineContext<S, E, T> context);

	/**
	 * Gets the source state of this transition.
	 *
	 * @return the source state
	 */
	State<S> getSource();

	/**
	 * Gets the target state of this transition.
	 *
	 * @return the target state
	 */
	State<S> getTarget();

	/**
	 * Gets the guard of this transition.
	 *
	 * @return the guard
	 */
	Guard<S, E, T> getGuard();

	/**
	 * Gets the transition actions.
	 *
	 * @return the transition actions
	 */
	Collection<Action<S, E, T>> getActions();

	/**
	 * Gets the transition trigger.
	 *
	 * @return the transition trigger
	 */
	Trigger<S, E> getTrigger();

	/**
	 * Gets the transition kind.
	 *
	 * @return the transition kind
	 */
	TransitionKind getKind();


	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	String getName();

}
