package com.example.api.service;

import com.example.api.dto.CountResponse;
import com.example.api.dto.CreateWarriorRequest;
import com.example.api.dto.WarriorResponse;
import com.example.api.entity.Warrior;
import com.example.api.exception.WarriorNotFoundException;
import com.example.api.repository.WarriorRepository;
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
    
    /**
     * Creates a new warrior and returns the created entity with generated UUID
     */
    @Transactional
    public WarriorResponse createWarrior(CreateWarriorRequest request) {
        log.info("Creating new warrior with name: {}", request.getName());
        
        Warrior warrior = Warrior.builder()
                .name(request.getName())
                .dob(request.getDob())
                .fightSkills(request.getFightSkills())
                .build();
        
        Warrior savedWarrior = warriorRepository.save(warrior);
        log.info("Warrior created with ID: {}", savedWarrior.getId());
        
        return mapToResponse(savedWarrior);
    }
    
    /**
     * Retrieves a warrior by ID
     */
    @Transactional(readOnly = true)
    public WarriorResponse getWarriorById(UUID id) {
        log.info("Fetching warrior with ID: {}", id);

        Warrior warrior = warriorRepository.findById(id)
                .orElseThrow(() -> new WarriorNotFoundException(id));
        
        return mapToResponse(warrior);
    }
    
    /**
     * Searches warriors by name or fight skills
     */
    @Transactional(readOnly = true)
    public List<WarriorResponse> searchWarriors(String term) {
        log.info("Searching warriors with term: {}", term);
        
        if (term == null || term.trim().isEmpty()) {
            // Return all warriors if no search term provided
            return warriorRepository.findAll(PageRequest.of(0, 50)).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        List<Warrior> warriors = warriorRepository.searchByNameOrSkills(
            term.trim(), PageRequest.of(0, 50));
        log.info("Found {} warriors matching term: {}", warriors.size(), term);
        
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
        log.info("Total warriors count: {}", count);
        
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
