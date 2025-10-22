package com.example.api.service;

import com.example.api.dto.CountResponse;
import com.example.api.dto.CreateWarriorRequest;
import com.example.api.dto.WarriorResponse;
import com.example.api.entity.Warrior;
import com.example.api.exception.WarriorNotFoundException;
import com.example.api.repository.WarriorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarriorServiceTest {
    
    @Mock
    private WarriorRepository warriorRepository;
    
    @InjectMocks
    private WarriorService warriorService;
    
    private Warrior testWarrior;
    private CreateWarriorRequest createRequest;
    
    @BeforeEach
    void setUp() {
        testWarrior = Warrior.builder()
                .id(UUID.randomUUID())
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
    void createWarrior_ShouldReturnCreatedWarrior() {
        // Given
        when(warriorRepository.save(any(Warrior.class))).thenReturn(testWarrior);
        
        // When
        WarriorResponse response = warriorService.createWarrior(createRequest);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(testWarrior.getId());
        assertThat(response.getName()).isEqualTo("Achilles");
        assertThat(response.getDob()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(response.getFightSkills()).containsExactly("Swordsmanship", "Shield Combat");
        
        verify(warriorRepository, times(1)).save(any(Warrior.class));
    }
    
    @Test
    void getWarriorById_WhenWarriorExists_ShouldReturnWarrior() {
        // Given
        UUID id = testWarrior.getId();
        when(warriorRepository.findById(id)).thenReturn(Optional.of(testWarrior));
        
        // When
        WarriorResponse response = warriorService.getWarriorById(id);
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("Achilles");
        
        verify(warriorRepository, times(1)).findById(id);
    }
    
    @Test
    void getWarriorById_WhenWarriorNotFound_ShouldThrowException() {
        // Given
        UUID id = UUID.randomUUID();
        when(warriorRepository.findById(id)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> warriorService.getWarriorById(id))
                .isInstanceOf(WarriorNotFoundException.class)
                .hasMessageContaining("Warrior not found with id: " + id);
        
        verify(warriorRepository, times(1)).findById(id);
    }
    
    @Test
    void searchWarriors_WithTerm_ShouldReturnMatchingWarriors() {
        // Given
        String searchTerm = "Achi";
        List<Warrior> warriors = Arrays.asList(testWarrior);
        when(warriorRepository.searchByNameOrSkills(searchTerm)).thenReturn(warriors);
        
        // When
        List<WarriorResponse> responses = warriorService.searchWarriors(searchTerm);
        
        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("Achilles");
        
        verify(warriorRepository, times(1)).searchByNameOrSkills(searchTerm);
    }
    
    @Test
    void searchWarriors_WithoutTerm_ShouldReturnAllWarriors() {
        // Given
        List<Warrior> warriors = Arrays.asList(testWarrior);
        when(warriorRepository.findAll()).thenReturn(warriors);
        
        // When
        List<WarriorResponse> responses = warriorService.searchWarriors(null);
        
        // Then
        assertThat(responses).hasSize(1);
        
        verify(warriorRepository, times(1)).findAll();
        verify(warriorRepository, never()).searchByNameOrSkills(any());
    }
    
    @Test
    void getWarriorCount_ShouldReturnCorrectCount() {
        // Given
        when(warriorRepository.count()).thenReturn(5L);
        
        // When
        CountResponse response = warriorService.getWarriorCount();
        
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCount()).isEqualTo(5L);
        
        verify(warriorRepository, times(1)).count();
    }
}
