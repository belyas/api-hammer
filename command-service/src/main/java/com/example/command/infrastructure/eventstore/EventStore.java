package com.example.command.infrastructure.eventstore;

import com.example.shared.events.DomainEvent;
import java.util.List;
import java.util.UUID;

/**
 * Event Store interface for persisting domain events
 */
public interface EventStore {
    void save(UUID aggregateId, List<DomainEvent> events, int expectedVersion);
    List<DomainEvent> getEvents(UUID aggregateId);
}
