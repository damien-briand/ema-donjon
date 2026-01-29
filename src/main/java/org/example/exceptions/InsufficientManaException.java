package org.example.exceptions;

public class InsufficientManaException extends Exception {
    private final int required;
    private final int available;

    public InsufficientManaException(int required, int available) {
        super("Mana insuffisant! Requis: " + required + ", Disponible: " + available);
        this.required = required;
        this.available = available;
    }

    public int getRequired() { return required; }
    public int getAvailable() { return available; }
}
