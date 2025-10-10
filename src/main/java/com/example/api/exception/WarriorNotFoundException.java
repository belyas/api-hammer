package com.example.api.exception;

import java.util.UUID;

public class WarriorNotFoundException extends RuntimeException {
    
    public WarriorNotFoundException(UUID id) {
        super("Warrior not found with id: " + id);
    }
}
