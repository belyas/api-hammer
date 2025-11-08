package com.example.query.readmodel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Warrior Read Model
 */
@Repository
public interface WarriorReadModelRepository extends JpaRepository<WarriorReadModel, UUID> {
    
    /**
     * Search warriors by name (case-insensitive)
     */
    List<WarriorReadModel> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find top N most recent warriors
     */
    List<WarriorReadModel> findTop50ByOrderByCreatedAtDesc();
    
    /**
     * Search by name or skills
     */
    @Query("SELECT w FROM WarriorReadModel w WHERE " +
           "LOWER(w.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "EXISTS (SELECT s FROM w.fightSkills s WHERE LOWER(s) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<WarriorReadModel> searchByTerm(@Param("term") String term);
}
