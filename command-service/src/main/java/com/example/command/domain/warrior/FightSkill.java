package com.example.command.domain.warrior;

import lombok.Value;

/**
 * Value Object representing a fighting skill
 * Enforces business rules on skill names
 */
@Value
public class FightSkill {
    String name;
    
    public FightSkill(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Fight skill cannot be empty");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Fight skill name too long (max 50 chars)");
        }
        this.name = name.trim();
    }
}
