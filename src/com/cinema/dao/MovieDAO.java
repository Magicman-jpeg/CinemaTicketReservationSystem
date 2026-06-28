package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Movie;
import java.util.*;

/**
 * Data Access Object for Movie entity (with genre, status, age_rating lookups).
 */
public class MovieDAO {

    private final DatabaseManager db;

    public MovieDAO() { this.db = DatabaseManager.getInstance(); }

    public List<Movie> findAll() throws DatabaseConnectionException {
        String sql = "SELECT m.*, g.movie_genre, s.movie_status, a.movie_age_rating " +
                     "FROM movie m " +
                     "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
                     "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
                     "LEFT JOIN age_rating a ON m.age_rate_id = a.age_rate_id " +
                     "ORDER BY m.movie_id";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Movie> movies = new ArrayList<>();
        for (Map<String, String> row : results) movies.add(mapToMovie(row));
        return movies;
    }

    public Movie findById(int movieId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT m.*, g.movie_genre, s.movie_status, a.movie_age_rating " +
            "FROM movie m " +
            "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
            "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
            "LEFT JOIN age_rating a ON m.age_rate_id = a.age_rate_id " +
            "WHERE m.movie_id = %d", movieId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToMovie(results.get(0));
    }

    public List<Movie> searchByTitle(String keyword) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT m.*, g.movie_genre, s.movie_status, a.movie_age_rating " +
            "FROM movie m " +
            "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
            "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
            "LEFT JOIN age_rating a ON m.age_rate_id = a.age_rate_id " +
            "WHERE m.movie_title LIKE '%%%s%%' ORDER BY m.movie_title",
            DatabaseManager.escapeString(keyword));
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Movie> movies = new ArrayList<>();
        for (Map<String, String> row : results) movies.add(mapToMovie(row));
        return movies;
    }

    public List<Movie> findByStatus(String status) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT m.*, g.movie_genre, s.movie_status, a.movie_age_rating " +
            "FROM movie m " +
            "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
            "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
            "LEFT JOIN age_rating a ON m.age_rate_id = a.age_rate_id " +
            "WHERE s.movie_status = '%s' ORDER BY m.movie_title",
            DatabaseManager.escapeString(status));
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Movie> movies = new ArrayList<>();
        for (Map<String, String> row : results) movies.add(mapToMovie(row));
        return movies;
    }

    public int insert(Movie movie) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO movie (movie_title, genre_id, movie_duration, duration_code, release_date, status_id, age_rate_id) " +
            "VALUES ('%s', %d, '%s', %d, '%s', %d, %d)",
            DatabaseManager.escapeString(movie.getMovieTitle()), movie.getGenreId(),
            DatabaseManager.escapeString(movie.getMovieDuration()), movie.getDurationCode(),
            DatabaseManager.escapeString(movie.getReleaseDate()), movie.getStatusId(), movie.getAgeRateId());
        return db.executeInsert(sql);
    }

    public boolean update(Movie movie) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE movie SET movie_title='%s', genre_id=%d, movie_duration='%s', " +
            "duration_code=%d, release_date='%s', status_id=%d, age_rate_id=%d WHERE movie_id=%d",
            DatabaseManager.escapeString(movie.getMovieTitle()), movie.getGenreId(),
            DatabaseManager.escapeString(movie.getMovieDuration()), movie.getDurationCode(),
            DatabaseManager.escapeString(movie.getReleaseDate()), movie.getStatusId(),
            movie.getAgeRateId(), movie.getMovieId());
        db.executeUpdate(sql);
        return true;
    }

    public boolean delete(int movieId) throws DatabaseConnectionException {
        db.executeUpdate(String.format("DELETE FROM movie WHERE movie_id = %d", movieId));
        return true;
    }

    public int getCount() throws DatabaseConnectionException { return db.getRowCount("movie"); }

    public List<Map<String, String>> getGenres() throws DatabaseConnectionException {
        return db.executeQuery("SELECT * FROM movie_genre ORDER BY genre_id");
    }

    public List<Map<String, String>> getStatuses() throws DatabaseConnectionException {
        return db.executeQuery("SELECT * FROM movie_status ORDER BY status_id");
    }

    public List<Map<String, String>> getAgeRatings() throws DatabaseConnectionException {
        return db.executeQuery("SELECT * FROM age_rating ORDER BY age_rate_id");
    }

    private Movie mapToMovie(Map<String, String> row) {
        Movie m = new Movie();
        m.setMovieId(parseInt(row.get("movie_id")));
        m.setMovieTitle(row.get("movie_title"));
        m.setGenreId(parseInt(row.get("genre_id")));
        m.setMovieDuration(row.get("movie_duration"));
        m.setDurationCode(parseInt(row.get("duration_code")));
        m.setReleaseDate(row.get("release_date"));
        m.setStatusId(parseInt(row.get("status_id")));
        m.setAgeRateId(parseInt(row.get("age_rate_id")));
        m.setGenreName(row.get("movie_genre"));
        m.setStatusName(row.get("movie_status"));
        m.setAgeRating(row.get("movie_age_rating"));
        return m;
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
}
