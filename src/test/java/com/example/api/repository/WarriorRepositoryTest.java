package com.example.api.repository;

import com.example.api.entity.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WarriorRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private WarriorRepository warriorRepository;
    
    private Warrior warrior1;
    private Warrior warrior2;
    
    @BeforeEach
    void setUp() {
        warrior1 = Warrior.builder()
                .name("Achilles")
                .dob(LocalDate.of(1990, 5, 15))
                .fightSkills(Arrays.asList("Swordsmanship", "Shield Combat"))
                .build();
        
        warrior2 = Warrior.builder()
                .name("Hector")
                .dob(LocalDate.of(1988, 3, 20))
                .fightSkills(Arrays.asList("Spear Fighting", "Archery"))
                .build();
        
        entityManager.persist(warrior1);
        entityManager.persist(warrior2);
        entityManager.flush();
    }
    
    @Test
    void save_ShouldPersistWarriorWithGeneratedId() {
        // Given
        Warrior newWarrior = Warrior.builder()
                .name("Leonidas")
                .dob(LocalDate.of(1985, 7, 10))
                .fightSkills(Arrays.asList("Shield Combat"))
                .build();
        
        // When
        Warrior saved = warriorRepository.save(newWarrior);
        
        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Leonidas");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
    
    @Test
    void findById_WhenExists_ShouldReturnWarrior() {
        // When
        Optional<Warrior> found = warriorRepository.findById(warrior1.getId());
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Achilles");
    }
    
    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingWarriors() {
        // When
        List<Warrior> found = warriorRepository.findByNameContainingIgnoreCase("ach");
        
        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Achilles");
    }
    
    @Test
    void searchByNameOrSkills_WithNameMatch_ShouldReturnWarriors() {
        // When
        List<Warrior> found = warriorRepository.searchByNameOrSkills("Hec");
        
        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Hector");
    }
    
    @Test
    void searchByNameOrSkills_WithSkillMatch_ShouldReturnWarriors() {
        // When
        List<Warrior> found = warriorRepository.searchByNameOrSkills("Archery");
        
        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Hector");
    }
    
    @Test
    void searchByNameOrSkills_WithCommonSkill_ShouldReturnMultipleWarriors() {
        // When
        List<Warrior> found = warriorRepository.searchByNameOrSkills("Combat");
        
        // Then - Both warriors have "Combat" in their skills
        assertThat(found).hasSize(2);
    }
    
    @Test
    void count_ShouldReturnCorrectCount() {
        // When
        long count = warriorRepository.count();
        
        // Then
        assertThat(count).isEqualTo(2);
    }
}
