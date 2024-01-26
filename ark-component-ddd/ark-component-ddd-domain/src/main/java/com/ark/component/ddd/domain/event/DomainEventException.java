package com.ark.component.ddd.domain.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DomainEventException extends RuntimeException {

    private final String message;

}
