package com.example.command.domain.warrior;

import lombok.Value;
import java.util.UUID;

/**
 * Value Object representing a Warrior's unique identifier
 */
@Value
public class WarriorId {
    UUID value;
    
    public static WarriorId generate() {
        return new WarriorId(UUID.randomUUID());
    }
    
    public static WarriorId of(UUID value) {
        return new WarriorId(value);
    }
}
