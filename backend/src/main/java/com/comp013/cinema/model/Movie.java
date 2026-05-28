package com.comp013.cinema.model;

public class Movie {
    private final int movieId;
    private final String title;
    private final String duration;
    private final String releaseDate;

    public Movie(int movieId, String title, String duration, String releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
