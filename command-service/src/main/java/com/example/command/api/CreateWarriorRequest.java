package com.example.command.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for creating a warrior
 */
@Value
public class CreateWarriorRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be 1-100 characters")
    String name;
    
    @NotNull(message = "Date of birth is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    
    List<String> fightSkills;
}
