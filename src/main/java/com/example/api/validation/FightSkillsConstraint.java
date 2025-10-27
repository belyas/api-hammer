package com.example.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FightSkillsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FightSkillsConstraint {
    String message() default "Fight skills must have at most 20 items and a total of 250 characters";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}