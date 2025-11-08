package com.example.command.api;

import com.example.command.domain.warrior.Warrior;
import com.example.command.domain.warrior.WarriorId;
import com.example.command.infrastructure.eventstore.EventStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Command Handler for Warrior operations
 * Implements command pattern for CQRS write side
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarriorCommandHandler {
    
    private final EventStore eventStore;
    
    /**
     * Handle CreateWarrior command
     * Creates new aggregate and persists events
     */
    @Transactional
    public UUID handle(CreateWarriorRequest command) {
        WarriorId id = WarriorId.generate();
        
        // Create domain aggregate
        Warrior warrior = Warrior.create(
            id,
            command.getName(),
            command.getDob(),
            command.getFightSkills() != null ? command.getFightSkills() : java.util.List.of()
        );
        
        // Persist events to event store
        eventStore.save(
            id.getValue(),
            warrior.getUncommittedEvents(),
            0  // Expected version for new aggregate
        );
        
        warrior.markEventsAsCommitted();
        
        log.info("Created warrior: {} ({})", warrior.getName(), id.getValue());
        return id.getValue();
    }
}
