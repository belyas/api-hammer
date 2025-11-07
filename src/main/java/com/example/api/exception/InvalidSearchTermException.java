package com.example.api.exception;

public class InvalidSearchTermException extends RuntimeException {

    public InvalidSearchTermException(String message) {
        super(message);
    }
}
