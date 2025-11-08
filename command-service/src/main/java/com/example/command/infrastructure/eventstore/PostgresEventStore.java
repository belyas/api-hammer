package com.example.command.infrastructure.eventstore;

import com.example.shared.events.DomainEvent;
import com.example.shared.events.WarriorCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * PostgreSQL-based Event Store
 * Provides append-only event persistence with optimistic locking
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class PostgresEventStore implements EventStore {
    
    private final JdbcTemplate jdbcTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(UUID aggregateId, List<DomainEvent> events, int expectedVersion) {
        // Optimistic concurrency check
        Integer currentVersion = jdbcTemplate.queryForObject(
            "SELECT MAX(event_version) FROM event_store WHERE aggregate_id = ?",
            Integer.class,
            aggregateId
        );
        
        if (currentVersion != null && currentVersion != expectedVersion) {
            throw new ConcurrencyException(
                String.format("Aggregate %s modified. Expected v%d, found v%d",
                    aggregateId, expectedVersion, currentVersion)
            );
        }
        
        // Append events to event store
        for (DomainEvent event : events) {
            String eventData = toJson(event);
            
            jdbcTemplate.update(
                "INSERT INTO event_store " +
                "(event_id, aggregate_id, aggregate_type, event_type, " +
                "event_version, event_data, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?::jsonb, ?)",
                event.getEventId(),
                event.getAggregateId(),
                "Warrior",
                event.getEventType(),
                event.getVersion(),
                eventData,
                Timestamp.from(event.getOccurredAt())
            );
            
            // Publish to Kafka for projections
            publishEventAsync(event);
            
            log.debug("Persisted event: {} v{} for aggregate {}", 
                event.getEventType(), event.getVersion(), aggregateId);
        }
    }
    
    @Override
    public List<DomainEvent> getEvents(UUID aggregateId) {
        return jdbcTemplate.query(
            "SELECT event_data, event_type " +
            "FROM event_store " +
            "WHERE aggregate_id = ? " +
            "ORDER BY event_version ASC",
            (rs, rowNum) -> deserializeEvent(
                rs.getString("event_data"),
                rs.getString("event_type")
            ),
            aggregateId
        );
    }
    
    private void publishEventAsync(DomainEvent event) {
        try {
            String key = event.getAggregateId().toString();
            String value = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send("warrior-events", key, value)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event {}", event.getEventId(), ex);
                    }
                });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}", event.getEventId(), e);
        }
    }
    
    private DomainEvent deserializeEvent(String json, String eventType) {
        try {
            return switch (eventType) {
                case "WarriorCreated" -> 
                    objectMapper.readValue(json, WarriorCreatedEvent.class);
                default -> 
                    throw new IllegalArgumentException("Unknown event: " + eventType);
            };
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
    
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize", e);
        }
    }
}
