package com.cinema.model;

/**
 * Represents a Cinema Seat (from cinema_seat table in Excel).
 * Fields: seat_no (TEXT, e.g. "A1"), row (TEXT), col (INTEGER)
 * Seat availability is tracked per screening in the transaction table.
 */
public class CinemaSeat {

    private String seatNo;  // e.g. "A1", "J10"
    private String row;     // A through J
    private int col;        // 1 through 10

    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_RESERVED = "RESERVED";

    public CinemaSeat() {}

    public CinemaSeat(String seatNo, String row, int col) {
        this.seatNo = seatNo;
        this.row = row;
        this.col = col;
    }

    public String getSeatNo() { return seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }
    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    @Override
    public String toString() {
        return String.format("| %-6s | %-3s | %-3d |", seatNo, row, col);
    }

    public static String getTableHeader() {
        return String.format("| %-6s | %-3s | %-3s |", "Seat", "Row", "Col");
    }

    public static String getTableDivider() {
        return "+--------+-----+-----+";
    }
}
