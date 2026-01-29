package org.example.exceptions;

public class InsufficientManaException extends RuntimeException {
    public InsufficientManaException(String message) {
        super(message);
    }
}
