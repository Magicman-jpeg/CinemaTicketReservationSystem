package com.cinema.model;

/**
 * Represents a Movie (from movie table in Excel).
 * Fields: movie_id, movie_title, genre_id, movie_duration, duration_code, release_date, status_id, age_rate_id
 */
public class Movie {

    private int movieId;
    private String movieTitle;
    private int genreId;
    private String movieDuration;   // e.g. "2 hrs 07 mins"
    private int durationCode;       // e.g. 207
    private String releaseDate;
    private int statusId;
    private int ageRateId;

    // Resolved lookup values (set after JOIN or manual lookup)
    private String genreName;
    private String statusName;
    private String ageRating;

    public Movie() {}

    public Movie(int movieId, String movieTitle, int genreId, String movieDuration,
                 int durationCode, String releaseDate, int statusId, int ageRateId) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.genreId = genreId;
        this.movieDuration = movieDuration;
        this.durationCode = durationCode;
        this.releaseDate = releaseDate;
        this.statusId = statusId;
        this.ageRateId = ageRateId;
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }
    public String getMovieDuration() { return movieDuration; }
    public void setMovieDuration(String movieDuration) { this.movieDuration = movieDuration; }
    public int getDurationCode() { return durationCode; }
    public void setDurationCode(int durationCode) { this.durationCode = durationCode; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }
    public int getAgeRateId() { return ageRateId; }
    public void setAgeRateId(int ageRateId) { this.ageRateId = ageRateId; }
    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }

    @Override
    public String toString() {
        return String.format("| %-3d | %-32s | %-10s | %-14s | %-10s | %-15s | %-4s |",
                movieId, movieTitle,
                genreName != null ? genreName : "ID:" + genreId,
                movieDuration, releaseDate,
                statusName != null ? statusName : "ID:" + statusId,
                ageRating != null ? ageRating : "ID:" + ageRateId);
    }

    public static String getTableHeader() {
        return String.format("| %-3s | %-32s | %-10s | %-14s | %-10s | %-15s | %-4s |",
                "ID", "Title", "Genre", "Duration", "Released", "Status", "Rate");
    }

    public static String getTableDivider() {
        return "+-----+----------------------------------+------------+----------------+------------+-----------------+------+";
    }
}
