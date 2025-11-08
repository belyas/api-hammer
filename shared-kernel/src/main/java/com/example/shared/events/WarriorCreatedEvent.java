package com.example.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Event published when a new warrior is created.
 * This is the source of truth for warrior creation.
 */
@Value
@Builder
public class WarriorCreatedEvent implements DomainEvent {
    UUID eventId;
    UUID aggregateId;
    int version;
    Instant occurredAt;
    
    // Domain data
    String name;
    LocalDate dob;
    List<String> fightSkills;
    
    @JsonCreator
    public WarriorCreatedEvent(
        @JsonProperty("eventId") UUID eventId,
        @JsonProperty("aggregateId") UUID aggregateId,
        @JsonProperty("version") int version,
        @JsonProperty("occurredAt") Instant occurredAt,
        @JsonProperty("name") String name,
        @JsonProperty("dob") LocalDate dob,
        @JsonProperty("fightSkills") List<String> fightSkills) {
        
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.version = version;
        this.occurredAt = occurredAt;
        this.name = name;
        this.dob = dob;
        this.fightSkills = fightSkills;
    }
    
    @Override
    public String getEventType() {
        return "WarriorCreated";
    }
}
