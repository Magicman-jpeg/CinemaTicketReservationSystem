package com.cinema.model;

/**
 * Represents a seat in the cinema hall.
 * Rows are labeled A-K, columns 1-10 (110 seats per hall).
 * Demonstrates encapsulation and status tracking for reservations.
 */
public class CinemaSeat {

    private int seatId;
    private int screeningId;
    private String seatRow;    // A through K
    private int seatColumn;    // 1 through 10
    private String status;     // AVAILABLE, RESERVED, OCCUPIED

    // Constants for seat status
    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_RESERVED = "RESERVED";
    public static final String STATUS_OCCUPIED = "OCCUPIED";

    // Default constructor
    public CinemaSeat() {
        this.status = STATUS_AVAILABLE;
    }

    // Parameterized constructor
    public CinemaSeat(int seatId, int screeningId, String seatRow,
                      int seatColumn, String status) {
        this.seatId = seatId;
        this.screeningId = screeningId;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.status = status;
    }

    // Constructor without ID
    public CinemaSeat(int screeningId, String seatRow, int seatColumn, String status) {
        this.screeningId = screeningId;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.status = status;
    }

    // Getters and Setters
    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        this.seatColumn = seatColumn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the seat label (e.g., "A5", "K10").
     */
    public String getSeatLabel() {
        return seatRow + seatColumn;
    }

    /**
     * Checks if the seat is available for reservation.
     */
    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(this.status);
    }

    /**
     * Reserves the seat (changes status to RESERVED).
     */
    public void reserve() {
        this.status = STATUS_RESERVED;
    }

    /**
     * Releases the seat (changes status back to AVAILABLE).
     */
    public void release() {
        this.status = STATUS_AVAILABLE;
    }

    @Override
    public String toString() {
        return String.format("| %-6d | %-11d | %-4s | %-6d | %-9s |",
                seatId, screeningId, seatRow, seatColumn, status);
    }

    public static String getTableHeader() {
        return String.format("| %-6s | %-11s | %-4s | %-6s | %-9s |",
                "SeatID", "ScreeningID", "Row", "Col", "Status");
    }

    public static String getTableDivider() {
        return "+--------+-------------+------+--------+-----------+";
    }
}
