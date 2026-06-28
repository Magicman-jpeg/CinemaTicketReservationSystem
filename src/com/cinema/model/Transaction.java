package com.cinema.model;

/**
 * Represents a ticket purchase/reservation transaction.
 * Links a Customer to a specific Screening and Seat.
 */
public class Transaction {

    private int transactionId;
    private int customerId;
    private int screeningId;
    private int seatId;
    private String seatLabel;        // e.g., "A5"
    private String transactionDate;
    private String transactionTime;
    private double amountPaid;
    private String paymentMethod;    // CASH, CREDIT_CARD, DEBIT_CARD, GCASH
    private String status;           // CONFIRMED, CANCELLED, PENDING

    // Constants for transaction status
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_PENDING = "PENDING";

    // Constants for payment methods
    public static final String PAY_CASH = "CASH";
    public static final String PAY_CREDIT = "CREDIT_CARD";
    public static final String PAY_DEBIT = "DEBIT_CARD";
    public static final String PAY_GCASH = "GCASH";

    // Default constructor
    public Transaction() {
        this.status = STATUS_PENDING;
    }

    // Parameterized constructor
    public Transaction(int transactionId, int customerId, int screeningId, int seatId,
                       String seatLabel, String transactionDate, String transactionTime,
                       double amountPaid, String paymentMethod, String status) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.screeningId = screeningId;
        this.seatId = seatId;
        this.seatLabel = seatLabel;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Constructor without ID
    public Transaction(int customerId, int screeningId, int seatId,
                       String seatLabel, String transactionDate, String transactionTime,
                       double amountPaid, String paymentMethod, String status) {
        this.customerId = customerId;
        this.screeningId = screeningId;
        this.seatId = seatId;
        this.seatLabel = seatLabel;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Confirms the transaction.
     */
    public void confirm() {
        this.status = STATUS_CONFIRMED;
    }

    /**
     * Cancels the transaction.
     */
    public void cancel() {
        this.status = STATUS_CANCELLED;
    }

    /**
     * Checks if the transaction is confirmed.
     */
    public boolean isConfirmed() {
        return STATUS_CONFIRMED.equals(this.status);
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-6d | %-6d | %-5s | %-10s | %-8s | PHP %8.2f | %-11s | %-9s |",
                transactionId, customerId, screeningId, seatLabel,
                transactionDate, transactionTime, amountPaid, paymentMethod, status);
    }

    public static String getTableHeader() {
        return String.format("| %-5s | %-6s | %-6s | %-5s | %-10s | %-8s | %-12s | %-11s | %-9s |",
                "TxnID", "CustID", "ScrID", "Seat", "Date", "Time", "Amount", "Payment", "Status");
    }

    public static String getTableDivider() {
        return "+-------+--------+--------+-------+------------+----------+--------------+-------------+-----------+";
    }
}
