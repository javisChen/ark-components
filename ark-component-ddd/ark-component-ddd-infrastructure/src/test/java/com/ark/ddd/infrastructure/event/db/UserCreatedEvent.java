package com.ark.ddd.infrastructure.event.db;

import com.ark.ddd.domain.event.DomainEvent;

public class UserCreatedEvent extends DomainEvent {

    private Long userId;

    protected UserCreatedEvent(Long userId) {
        super("UserCreatedEvent", userId);
    }
}
