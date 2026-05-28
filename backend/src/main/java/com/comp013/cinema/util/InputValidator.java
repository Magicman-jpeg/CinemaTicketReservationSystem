package com.comp013.cinema.util;

import com.comp013.cinema.exception.InvalidSeatException;

public final class InputValidator {
    private InputValidator() {
    }

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
    }

    public static int validatePositiveInt(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number.");
        }
        return value;
    }

    public static String validateSeatNo(String seatNo) throws InvalidSeatException {
        if (seatNo == null || !seatNo.matches("^[A-K](10|[1-9])$")) {
            throw new InvalidSeatException("Seat number must be in A1 to K10 format.");
        }
        return seatNo;
    }
}
