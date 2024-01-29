package com.ark.component.ddd.infrastructure.event;

import com.ark.component.ddd.domain.event.DomainEvent;
import java.util.ArrayList;
import java.util.List;

import static java.lang.ThreadLocal.withInitial;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class ThreadLocalDomainEventIdHolder {
    private static final ThreadLocal<List<Long>> THREAD_LOCAL_EVENT_IDS = withInitial(ArrayList::new);

    public static void clear() {
        eventIds().clear();
    }

    public static void remove() {
        THREAD_LOCAL_EVENT_IDS.remove();
    }

    public static List<Long> allEventIds() {
        List<Long> eventIds = eventIds();
        return isNotEmpty(eventIds) ? List.copyOf(eventIds) : List.of();
    }

    public static void addEvents(List<DomainEvent> events) {
        events.forEach(ThreadLocalDomainEventIdHolder::addEvent);
    }

    public static void addEvent(DomainEvent event) {
        List<Long> eventIds = eventIds();
        eventIds.add(event.getId());
    }

    private static List<Long> eventIds() {
        return THREAD_LOCAL_EVENT_IDS.get();
    }

}
