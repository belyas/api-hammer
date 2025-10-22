package com.example.api.controller;

import com.example.api.dto.CountResponse;
import com.example.api.dto.CreateWarriorRequest;
import com.example.api.dto.WarriorResponse;
import com.example.api.exception.WarriorNotFoundException;
import com.example.api.service.WarriorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarriorController.class)
class WarriorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private WarriorService warriorService;
    
    private WarriorResponse warriorResponse;
    private CreateWarriorRequest createRequest;
    
    @BeforeEach
    void setUp() {
        UUID id = UUID.randomUUID();
        
        warriorResponse = WarriorResponse.builder()
                .id(id)
                .name("Achilles")
                .dob(LocalDate.of(1990, 5, 15))
                .fightSkills(Arrays.asList("Swordsmanship", "Shield Combat"))
                .build();
        
        createRequest = CreateWarriorRequest.builder()
                .name("Achilles")
                .dob(LocalDate.of(1990, 5, 15))
                .fightSkills(Arrays.asList("Swordsmanship", "Shield Combat"))
                .build();
    }
    
    @Test
    void createWarrior_WithValidRequest_ShouldReturnCreated() throws Exception {
        // Given
        when(warriorService.createWarrior(any(CreateWarriorRequest.class)))
                .thenReturn(warriorResponse);
        
        // When & Then
        mockMvc.perform(post("/warrior")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(warriorResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value("Achilles"))
                .andExpect(jsonPath("$.dob").value("1990-05-15"))
                .andExpect(jsonPath("$.fightSkills", hasSize(2)));
        
        verify(warriorService, times(1)).createWarrior(any(CreateWarriorRequest.class));
    }
    
    @Test
    void createWarrior_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given - create request without required name
        CreateWarriorRequest invalidRequest = CreateWarriorRequest.builder()
                .dob(LocalDate.of(1990, 5, 15))
                .build();
        
        // When & Then
        mockMvc.perform(post("/warrior")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
        
        verify(warriorService, never()).createWarrior(any(CreateWarriorRequest.class));
    }
    
    @Test
    void getWarriorById_WhenExists_ShouldReturnWarrior() throws Exception {
        // Given
        UUID id = warriorResponse.getId();
        when(warriorService.getWarriorById(id)).thenReturn(warriorResponse);
        
        // When & Then
        mockMvc.perform(get("/warrior/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Achilles"));
        
        verify(warriorService, times(1)).getWarriorById(id);
    }
    
    @Test
    void getWarriorById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        when(warriorService.getWarriorById(id))
                .thenThrow(new WarriorNotFoundException(id));
        
        // When & Then
        mockMvc.perform(get("/warrior/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Warrior not found with id: " + id));
        
        verify(warriorService, times(1)).getWarriorById(id);
    }
    
    @Test
    void searchWarriors_WithTerm_ShouldReturnMatchingWarriors() throws Exception {
        // Given
        String searchTerm = "Achi";
        List<WarriorResponse> warriors = Arrays.asList(warriorResponse);
        when(warriorService.searchWarriors(searchTerm)).thenReturn(warriors);
        
        // When & Then
        mockMvc.perform(get("/warrior")
                .param("t", searchTerm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Achilles"));
        
        verify(warriorService, times(1)).searchWarriors(searchTerm);
    }
    
    @Test
    void searchWarriors_WithoutTerm_ShouldReturnAllWarriors() throws Exception {
        // Given
        List<WarriorResponse> warriors = Arrays.asList(warriorResponse);
        when(warriorService.searchWarriors(null)).thenReturn(warriors);
        
        // When & Then
        mockMvc.perform(get("/warrior"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        
        verify(warriorService, times(1)).searchWarriors(null);
    }
    
    @Test
    void getWarriorCount_ShouldReturnCount() throws Exception {
        // Given
        CountResponse countResponse = CountResponse.builder().count(10L).build();
        when(warriorService.getWarriorCount()).thenReturn(countResponse);
        
        // When & Then
        mockMvc.perform(get("/counting-warriors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(10));
        
        verify(warriorService, times(1)).getWarriorCount();
    }
}
