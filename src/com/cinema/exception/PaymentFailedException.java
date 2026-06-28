package com.cinema.exception;

/**
 * Thrown when a payment transaction fails or is rejected.
 */
public class PaymentFailedException extends Exception {

    private String paymentMethod;
    private double amount;

    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException(String message, String paymentMethod, double amount) {
        super(message);
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PaymentFailedException: " + getMessage()
                + " [Method: " + paymentMethod + ", Amount: PHP " + String.format("%.2f", amount) + "]";
    }
}
