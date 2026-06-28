package com.cinema.model;

/**
 * Represents a Screening (showtime) entity.
 * Links a Movie to a specific date, time, and cinema hall.
 */
public class Screening {

    private int screeningId;
    private int movieId;
    private String screenDate;
    private String screenTime;
    private int hallNumber;
    private double ticketPrice;

    // Default constructor
    public Screening() {
    }

    // Parameterized constructor
    public Screening(int screeningId, int movieId, String screenDate,
                     String screenTime, int hallNumber, double ticketPrice) {
        this.screeningId = screeningId;
        this.movieId = movieId;
        this.screenDate = screenDate;
        this.screenTime = screenTime;
        this.hallNumber = hallNumber;
        this.ticketPrice = ticketPrice;
    }

    // Constructor without ID (for new inserts)
    public Screening(int movieId, String screenDate, String screenTime,
                     int hallNumber, double ticketPrice) {
        this.movieId = movieId;
        this.screenDate = screenDate;
        this.screenTime = screenTime;
        this.hallNumber = hallNumber;
        this.ticketPrice = ticketPrice;
    }

    // Getters and Setters
    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getScreenDate() {
        return screenDate;
    }

    public void setScreenDate(String screenDate) {
        this.screenDate = screenDate;
    }

    public String getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(String screenTime) {
        this.screenTime = screenTime;
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-8d | %-10s | %-8s | Hall %-2d | PHP %.2f |",
                screeningId, movieId, screenDate, screenTime, hallNumber, ticketPrice);
    }

    public static String getTableHeader() {
        return String.format("| %-4s | %-8s | %-10s | %-8s | %-7s | %-10s |",
                "ID", "MovieID", "Date", "Time", "Hall", "Price");
    }

    public static String getTableDivider() {
        return "+------+----------+------------+----------+---------+------------+";
    }
}
