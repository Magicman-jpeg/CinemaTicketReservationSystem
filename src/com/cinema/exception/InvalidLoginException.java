package com.cinema.exception;

/**
 * Thrown when login credentials are invalid (wrong username/password).
 * Demonstrates custom exception handling in OOP.
 */
public class InvalidLoginException extends Exception {

    private String attemptedUsername;

    public InvalidLoginException(String message) {
        super(message);
    }

    public InvalidLoginException(String message, String attemptedUsername) {
        super(message);
        this.attemptedUsername = attemptedUsername;
    }

    public InvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getAttemptedUsername() {
        return attemptedUsername;
    }

    @Override
    public String toString() {
        return "InvalidLoginException: " + getMessage()
                + (attemptedUsername != null ? " [Username: " + attemptedUsername + "]" : "");
    }
}
