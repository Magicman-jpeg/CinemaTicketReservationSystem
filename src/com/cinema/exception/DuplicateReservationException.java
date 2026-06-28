package com.cinema.exception;

/**
 * Thrown when a customer attempts to reserve a seat that is already reserved
 * for the same screening.
 */
public class DuplicateReservationException extends Exception {

    private int customerId;
    private int screeningId;
    private String seatLabel;

    public DuplicateReservationException(String message) {
        super(message);
    }

    public DuplicateReservationException(String message, int customerId,
                                         int screeningId, String seatLabel) {
        super(message);
        this.customerId = customerId;
        this.screeningId = screeningId;
        this.seatLabel = seatLabel;
    }

    public DuplicateReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    @Override
    public String toString() {
        return "DuplicateReservationException: " + getMessage()
                + " [Customer: " + customerId + ", Screening: " + screeningId
                + ", Seat: " + seatLabel + "]";
    }
}
