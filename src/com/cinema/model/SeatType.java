package com.cinema.model;

/**
 * Represents a Seat Type lookup (from seat_type table in Excel).
 * Fields: seat_type_id, seat_type, seat_description, ticket_price
 */
public class SeatType {

    private int seatTypeId;
    private String seatType;        // VIP, Premium, Regular
    private String seatDescription;
    private double ticketPrice;

    public SeatType() {}

    public SeatType(int seatTypeId, String seatType, String seatDescription, double ticketPrice) {
        this.seatTypeId = seatTypeId;
        this.seatType = seatType;
        this.seatDescription = seatDescription;
        this.ticketPrice = ticketPrice;
    }

    public int getSeatTypeId() { return seatTypeId; }
    public void setSeatTypeId(int seatTypeId) { this.seatTypeId = seatTypeId; }
    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }
    public String getSeatDescription() { return seatDescription; }
    public void setSeatDescription(String seatDescription) { this.seatDescription = seatDescription; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    @Override
    public String toString() {
        return String.format("| %-2d | %-8s | PHP %6.2f | %-50s |",
                seatTypeId, seatType, ticketPrice, seatDescription);
    }

    public static String getTableHeader() {
        return String.format("| %-2s | %-8s | %-10s | %-50s |",
                "ID", "Type", "Price", "Description");
    }

    public static String getTableDivider() {
        return "+----+----------+------------+----------------------------------------------------+";
    }
}
