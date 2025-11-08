package com.example.command.domain.warrior;

import com.example.shared.events.DomainEvent;
import com.example.shared.events.WarriorCreatedEvent;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Warrior Aggregate Root
 * Encapsulates warrior business logic and state changes via events
 */
@Getter
public class Warrior {
    private final WarriorId id;
    private String name;
    private LocalDate dob;
    private List<FightSkill> fightSkills;
    private int version;
    private final List<DomainEvent> uncommittedEvents = new ArrayList<>();
    
    private Warrior(WarriorId id) {
        this.id = id;
    }
    
    /**
     * Factory method to create a new warrior
     * Generates WarriorCreatedEvent
     */
    public static Warrior create(
        WarriorId id, 
        String name, 
        LocalDate dob, 
        List<String> skills) {
        
        Warrior warrior = new Warrior(id);
        
        WarriorCreatedEvent event = WarriorCreatedEvent.builder()
            .eventId(UUID.randomUUID())
            .aggregateId(id.getValue())
            .version(1)
            .occurredAt(Instant.now())
            .name(name)
            .dob(dob)
            .fightSkills(skills)
            .build();
        
        warrior.apply(event);
        return warrior;
    }
    
    /**
     * Apply event to change state (for new events)
     */
    private void apply(WarriorCreatedEvent event) {
        this.name = event.getName();
        this.dob = event.getDob();
        this.fightSkills = event.getFightSkills().stream()
            .map(FightSkill::new)
            .collect(Collectors.toList());
        this.version = event.getVersion();
        this.uncommittedEvents.add(event);
    }
    
    /**
     * Reconstitute warrior from historical events
     */
    public static Warrior fromEvents(WarriorId id, List<DomainEvent> events) {
        Warrior warrior = new Warrior(id);
        
        for (DomainEvent event : events) {
            if (event instanceof WarriorCreatedEvent created) {
                warrior.applyHistoric(created);
            }
        }
        
        return warrior;
    }
    
    /**
     * Apply historical event (don't add to uncommitted)
     */
    private void applyHistoric(WarriorCreatedEvent event) {
        this.name = event.getName();
        this.dob = event.getDob();
        this.fightSkills = event.getFightSkills().stream()
            .map(FightSkill::new)
            .collect(Collectors.toList());
        this.version = event.getVersion();
    }
    
    public List<DomainEvent> getUncommittedEvents() {
        return new ArrayList<>(uncommittedEvents);
    }
    
    public void markEventsAsCommitted() {
        uncommittedEvents.clear();
    }
}
