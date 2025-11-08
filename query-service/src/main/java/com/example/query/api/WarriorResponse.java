package com.example.query.api;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Warrior response DTO for queries
 */
@Value
public class WarriorResponse {
    UUID id;
    String name;
    LocalDate dob;
    List<String> fightSkills;
}
