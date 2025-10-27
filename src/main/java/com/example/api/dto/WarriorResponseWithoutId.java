package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarriorResponseWithoutId {

    private String name;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    
    private List<String> fightSkills;
}
