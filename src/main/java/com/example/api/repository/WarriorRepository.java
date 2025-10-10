package com.example.api.repository;

import com.example.api.entity.Warrior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarriorRepository extends JpaRepository<Warrior, UUID> {
    
    // Search warriors by name containing the search term
    List<Warrior> findByNameContainingIgnoreCase(String name);
    
    // Custom query to search by name or fight skills
    @Query("SELECT DISTINCT w FROM Warrior w LEFT JOIN w.fightSkills s " +
           "WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
           "OR LOWER(s) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Warrior> searchByNameOrSkills(@Param("term") String term);
}
