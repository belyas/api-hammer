package com.example.api.service;

import com.example.api.dto.CountResponse;
import com.example.api.dto.CreateWarriorRequest;
import com.example.api.dto.WarriorResponse;
import com.example.api.dto.WarriorResponseWithoutId;
import com.example.api.entity.Warrior;
import com.example.api.exception.WarriorNotFoundException;
import com.example.api.repository.WarriorRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarriorService {
    
    private final WarriorRepository warriorRepository;
    private final EntityManager entityManager;
    
    /**
     * Creates a new warrior and returns the created entity with generated UUID
     * CRITICAL: Forces immediate flush+commit before returning 201 to client
     */
    @Transactional
    public WarriorResponseWithoutId createWarrior(CreateWarriorRequest request) {
        Warrior warrior = Warrior.builder()
                .name(request.getName())
                .dob(request.getDob())
                .fightSkills(request.getFightSkills())
                .build();
        
        Warrior savedWarrior = warriorRepository.saveAndFlush(warrior);
        entityManager.clear();
        
        return WarriorResponseWithoutId.builder()
                .name(savedWarrior.getName())
                .dob(savedWarrior.getDob())
                .fightSkills(savedWarrior.getFightSkills())
                .build();
    }
    
    /**
     * Retrieves a warrior by ID
     */
    @Transactional(readOnly = true)
    public WarriorResponse getWarriorById(UUID id) {
        Warrior warrior = warriorRepository.findById(id)
                .orElseThrow(() -> new WarriorNotFoundException(id));
        
        return mapToResponse(warrior);
    }
    
    /**
     * Searches warriors by name or fight skills
     */
    @Transactional(readOnly = true)
    public List<WarriorResponse> searchWarriors(String term) {
        if (term == null || term.trim().isEmpty()) {
            return warriorRepository.findAll(PageRequest.of(0, 50)).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        List<Warrior> warriors = warriorRepository.searchByNameOrSkills(
            term.trim(), PageRequest.of(0, 50));
        
        return warriors.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Returns the total count of warriors
     */
    @Transactional(readOnly = true)
    public CountResponse getWarriorCount() {
        long count = warriorRepository.count();
        return CountResponse.builder()
                .count(count)
                .build();
    }
    
    /**
     * Maps Warrior entity to WarriorResponse DTO
     */
    private WarriorResponse mapToResponse(Warrior warrior) {
        return WarriorResponse.builder()
                .id(warrior.getId())
                .name(warrior.getName())
                .dob(warrior.getDob())
                .fightSkills(warrior.getFightSkills())
                .build();
    }
}
