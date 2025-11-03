package com.example.command.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Command API Controller
 * Handles write operations (commands) in CQRS architecture
 */
@RestController
@RequestMapping("/api/v1/commands")
@RequiredArgsConstructor
@Slf4j
public class CommandController {
    
    private final WarriorCommandHandler commandHandler;
    
    /**
     * Create a new warrior
     * Returns 202 Accepted (async processing via events)
     */
    @PostMapping("/warriors")
    public ResponseEntity<WarriorCreatedResponse> createWarrior(
        @Valid @RequestBody CreateWarriorRequest request) {
        
        UUID warriorId = commandHandler.handle(request);
        
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .header("Location", "/api/v1/queries/warriors/" + warriorId)
            .body(new WarriorCreatedResponse(warriorId));
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Command service is running");
    }
}
