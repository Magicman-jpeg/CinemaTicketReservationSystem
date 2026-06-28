package com.cinema.exception;

/**
 * Thrown when an invalid seat is selected (out of range or already occupied).
 */
public class InvalidSeatException extends Exception {

    private String seatLabel;

    public InvalidSeatException(String message) {
        super(message);
    }

    public InvalidSeatException(String message, String seatLabel) {
        super(message);
        this.seatLabel = seatLabel;
    }

    public InvalidSeatException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    @Override
    public String toString() {
        return "InvalidSeatException: " + getMessage()
                + (seatLabel != null ? " [Seat: " + seatLabel + "]" : "");
    }
}
