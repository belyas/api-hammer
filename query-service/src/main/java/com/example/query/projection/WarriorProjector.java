package com.example.query.projection;

import com.example.query.readmodel.WarriorReadModel;
import com.example.query.readmodel.WarriorReadModelRepository;
import com.example.shared.events.WarriorCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Projects Warrior events into read model
 * Listens to Kafka events and updates read database
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarriorProjector {
    
    private final WarriorReadModelRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    
    /**
     * Listen to warrior events and project into read model
     */
    @KafkaListener(topics = "warrior-events", groupId = "warrior-read-projection")
    @Transactional
    public void projectEvent(String eventJson) {
        try {
            // Deserialize event
            WarriorCreatedEvent event = objectMapper.readValue(
                eventJson, 
                WarriorCreatedEvent.class
            );
            
            // Check if already processed (idempotency)
            if (repository.existsById(event.getAggregateId())) {
                log.debug("Event already processed: {}", event.getEventId());
                return;
            }
            
            // Project into read model
            WarriorReadModel readModel = new WarriorReadModel();
            readModel.setId(event.getAggregateId());
            readModel.setName(event.getName());
            readModel.setDob(event.getDob());
            readModel.setFightSkills(event.getFightSkills());
            readModel.setCreatedAt(event.getOccurredAt());
            readModel.setUpdatedAt(event.getOccurredAt());
            readModel.setVersion(event.getVersion());
            
            repository.save(readModel);
            
            log.info("Projected WarriorCreated: {} ({})", 
                event.getName(), event.getAggregateId());
                
        } catch (Exception e) {
            log.error("Failed to project event: {}", eventJson, e);
            // In production: send to DLQ for manual handling
        }
    }
}
