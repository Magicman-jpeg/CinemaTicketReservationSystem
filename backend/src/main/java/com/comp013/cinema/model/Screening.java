package com.comp013.cinema.model;

public class Screening {
    private final String screeningId;
    private final String screeningDate;
    private final String timeSlot;
    private final int movieId;

    public Screening(String screeningId, String screeningDate, String timeSlot, int movieId) {
        this.screeningId = screeningId;
        this.screeningDate = screeningDate;
        this.timeSlot = timeSlot;
        this.movieId = movieId;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public String getScreeningDate() {
        return screeningDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public int getMovieId() {
        return movieId;
    }
}
