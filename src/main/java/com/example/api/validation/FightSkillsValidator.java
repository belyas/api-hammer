package com.example.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class FightSkillsValidator implements ConstraintValidator<FightSkillsConstraint, List<String>> {

    private static final int MAX_ITEMS = 20;
    private static final int MAX_TOTAL_LENGTH = 250;

    @Override
    public boolean isValid(List<String> fightSkills, ConstraintValidatorContext context) {
        if (fightSkills == null) {
            return false;
        }

        if (fightSkills.stream().anyMatch(skill -> skill == null || skill.trim().isEmpty())) {
            return false;
        }

        if (fightSkills.size() > MAX_ITEMS) {
            return false;
        }

        int totalLength = fightSkills.stream().mapToInt(String::length).sum();
        return totalLength <= MAX_TOTAL_LENGTH;
    }
}