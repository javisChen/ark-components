package com.ark.ddd.infrastructure.event.publish;

import java.util.List;

public interface DomainEventPublisher {
    void publish(List<Long> eventIds);
}
