package com.example.shared.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base interface for all domain events in the system.
 * Events are immutable facts that have occurred in the domain.
 */
public interface DomainEvent {
    UUID getEventId();
    UUID getAggregateId();
    String getEventType();
    int getVersion();
    Instant getOccurredAt();
}
