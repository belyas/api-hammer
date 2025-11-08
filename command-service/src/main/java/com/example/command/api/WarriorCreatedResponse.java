package com.example.command.api;

import lombok.Value;
import java.util.UUID;

/**
 * Response DTO for warrior creation
 */
@Value
public class WarriorCreatedResponse {
    UUID id;
    String message;
    
    public WarriorCreatedResponse(UUID id) {
        this.id = id;
        this.message = "Warrior created successfully";
    }
}
