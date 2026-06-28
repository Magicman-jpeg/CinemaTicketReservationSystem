package com.cinema.model;

/**
 * Represents a Screening (from screenings table in Excel).
 * Fields: screening_id (TEXT), screening_day, screening_date, time_slot, seat_type_id, movie_id, cinema_no
 */
public class Screening {

    private String screeningId;     // e.g. "SUN-1", "MON-5", "TUE-12"
    private String screeningDay;    // SUN, MON, TUE
    private String screeningDate;   // YYYY-MM-DD
    private String timeSlot;        // HH:MM
    private int seatTypeId;
    private int movieId;
    private int cinemaNo;

    // Resolved values
    private String movieTitle;
    private String seatTypeName;
    private double ticketPrice;

    public Screening() {}

    public Screening(String screeningId, String screeningDay, String screeningDate,
                     String timeSlot, int seatTypeId, int movieId, int cinemaNo) {
        this.screeningId = screeningId;
        this.screeningDay = screeningDay;
        this.screeningDate = screeningDate;
        this.timeSlot = timeSlot;
        this.seatTypeId = seatTypeId;
        this.movieId = movieId;
        this.cinemaNo = cinemaNo;
    }

    public String getScreeningId() { return screeningId; }
    public void setScreeningId(String screeningId) { this.screeningId = screeningId; }
    public String getScreeningDay() { return screeningDay; }
    public void setScreeningDay(String screeningDay) { this.screeningDay = screeningDay; }
    public String getScreeningDate() { return screeningDate; }
    public void setScreeningDate(String screeningDate) { this.screeningDate = screeningDate; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public int getSeatTypeId() { return seatTypeId; }
    public void setSeatTypeId(int seatTypeId) { this.seatTypeId = seatTypeId; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public int getCinemaNo() { return cinemaNo; }
    public void setCinemaNo(int cinemaNo) { this.cinemaNo = cinemaNo; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getSeatTypeName() { return seatTypeName; }
    public void setSeatTypeName(String seatTypeName) { this.seatTypeName = seatTypeName; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    @Override
    public String toString() {
        return String.format("| %-7s | %-3s | %-10s | %-5s | Cinema %-2d | %-8s | PHP %6.2f | %-30s |",
                screeningId, screeningDay, screeningDate, timeSlot, cinemaNo,
                seatTypeName != null ? seatTypeName : "Type" + seatTypeId,
                ticketPrice,
                movieTitle != null ? movieTitle : "Movie" + movieId);
    }

    public static String getTableHeader() {
        return String.format("| %-7s | %-3s | %-10s | %-5s | %-9s | %-8s | %-10s | %-30s |",
                "ID", "Day", "Date", "Time", "Cinema", "SeatType", "Price", "Movie");
    }

    public static String getTableDivider() {
        return "+---------+-----+------------+-------+-----------+----------+------------+--------------------------------+";
    }
}
