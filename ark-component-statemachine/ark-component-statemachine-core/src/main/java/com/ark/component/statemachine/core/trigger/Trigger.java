package com.ark.component.statemachine.core.trigger;

import com.ark.component.statemachine.core.Event;

public interface Trigger<S,E> {
	Event<E> getEvent();

}