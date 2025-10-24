package com.example.api.controller;

import com.example.api.dto.CountResponse;
import com.example.api.dto.CreateWarriorRequest;
import com.example.api.dto.WarriorResponse;
import com.example.api.service.WarriorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class WarriorController {
    
    private final WarriorService warriorService;
    
    /**
     * POST /warrior - Create a new warrior
     */
    @PostMapping("/warrior")
    public ResponseEntity<WarriorResponse> createWarrior(
            @Valid @RequestBody CreateWarriorRequest request) {
        
        log.info("Received request to create warrior: {}", request.getName());
        WarriorResponse response = warriorService.createWarrior(request);
        String location = String.format("/name/%s", response.getId());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", location)
            .body(response);
    }
    
    /**
     * GET /warrior/:id - Get warrior by ID
     */
    @GetMapping("/warrior/{id}")
    public ResponseEntity<WarriorResponse> getWarriorById(@PathVariable UUID id) {
        log.info("Received request to get warrior with ID: {}", id);
        WarriorResponse response = warriorService.getWarriorById(id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /warrior?t=[:term] - Search warriors by name or skills
     */
    @GetMapping("/warrior")
    public ResponseEntity<List<WarriorResponse>> searchWarriors(
            @RequestParam(value = "t", required = false) String term) {
        
        log.info("Received request to search warriors with term: {}", term);
        List<WarriorResponse> warriors = warriorService.searchWarriors(term);
        
        return ResponseEntity.ok(warriors);
    }
    
    /**
     * GET /counting-warriors - Get total warrior count
     */
    @GetMapping("/counting-warriors")
    public ResponseEntity<CountResponse> getWarriorCount() {
        log.info("Received request to count warriors");
        CountResponse response = warriorService.getWarriorCount();
        
        return ResponseEntity.ok(response);
    }
}
