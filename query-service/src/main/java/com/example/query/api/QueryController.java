package com.example.query.api;

import com.example.query.readmodel.WarriorReadModel;
import com.example.query.readmodel.WarriorReadModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Query API Controller
 * Handles read operations (queries) in CQRS architecture
 */
@RestController
@RequestMapping("/api/v1/queries")
@RequiredArgsConstructor
@Slf4j
public class QueryController {
    
    private final WarriorReadModelRepository repository;
    
    /**
     * Get warrior by ID
     */
    @GetMapping("/warriors/{id}")
    public ResponseEntity<WarriorResponse> getWarrior(@PathVariable UUID id) {
        return repository.findById(id)
            .map(this::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Search warriors by term (name or skills)
     */
    @GetMapping("/warriors")
    public ResponseEntity<List<WarriorResponse>> searchWarriors(
        @RequestParam(required = false) String t) {
        
        List<WarriorReadModel> results;
        
        if (t == null || t.isBlank()) {
            // Return recent warriors
            results = repository.findTop50ByOrderByCreatedAtDesc();
        } else {
            // Search by term
            results = repository.searchByTerm(t);
        }
        
        List<WarriorResponse> response = results.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get total warrior count
     */
    @GetMapping("/counting-warriors")
    public ResponseEntity<CountResponse> getCount() {
        long count = repository.count();
        return ResponseEntity.ok(new CountResponse(count));
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Query service is running");
    }
    
    private WarriorResponse toResponse(WarriorReadModel model) {
        return new WarriorResponse(
            model.getId(),
            model.getName(),
            model.getDob(),
            model.getFightSkills()
        );
    }
    
    record CountResponse(long count) {}
}
