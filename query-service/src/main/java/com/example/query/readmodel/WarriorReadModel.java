package com.example.query.readmodel;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Read Model for Warrior queries
 * Optimized for fast reads, updated by event projections
 */
@Entity
@Table(name = "warriors_read_model")
@Data
@NoArgsConstructor
public class WarriorReadModel {
    
    @Id
    private UUID id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false)
    private LocalDate dob;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "warrior_skills", joinColumns = @JoinColumn(name = "warrior_id"))
    @Column(name = "skill")
    private List<String> fightSkills;
    
    @Column(nullable = false)
    private Instant createdAt;
    
    @Column(nullable = false)
    private Instant updatedAt;
    
    private int version;
}
