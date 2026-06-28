package com.cinema.model;

/**
 * Represents a Movie entity in the Cinema Ticket Reservation System.
 * Demonstrates encapsulation with private fields and public getters/setters.
 */
public class Movie {

    private int movieId;
    private String title;
    private String genre;
    private int durationMinutes;
    private String rating; // e.g., G, PG, PG-13, R
    private String director;
    private String releaseDate;

    // Default constructor
    public Movie() {
    }

    // Parameterized constructor
    public Movie(int movieId, String title, String genre, int durationMinutes,
                 String rating, String director, String releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.director = director;
        this.releaseDate = releaseDate;
    }

    // Constructor without ID (for new inserts)
    public Movie(String title, String genre, int durationMinutes,
                 String rating, String director, String releaseDate) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.director = director;
        this.releaseDate = releaseDate;
    }

    // Getters and Setters
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-30s | %-12s | %3d min | %-5s | %-20s | %-10s |",
                movieId, title, genre, durationMinutes, rating, director, releaseDate);
    }

    /**
     * Returns a formatted header for table display.
     */
    public static String getTableHeader() {
        return String.format("| %-4s | %-30s | %-12s | %-7s | %-5s | %-20s | %-10s |",
                "ID", "Title", "Genre", "Length", "Rate", "Director", "Release");
    }

    public static String getTableDivider() {
        return "+------+--------------------------------+--------------+---------+-------+----------------------+------------+";
    }
}
