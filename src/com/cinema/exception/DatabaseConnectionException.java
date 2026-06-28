package com.cinema.exception;

/**
 * Thrown when a database connection or query fails.
 */
public class DatabaseConnectionException extends Exception {

    private String operation;

    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, String operation) {
        super(message);
        this.operation = operation;
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConnectionException(String message, String operation, Throwable cause) {
        super(message, cause);
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "DatabaseConnectionException: " + getMessage()
                + (operation != null ? " [Operation: " + operation + "]" : "");
    }
}
