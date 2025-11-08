package com.example.command.infrastructure.eventstore;

/**
 * Thrown when optimistic locking fails during event append
 */
public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException(String message) {
        super(message);
    }
}
