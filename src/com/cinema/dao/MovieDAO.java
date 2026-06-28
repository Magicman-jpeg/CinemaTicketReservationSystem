package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Movie;

import java.util.*;

/**
 * Data Access Object for Movie entity.
 * Provides CRUD operations for the movie table.
 */
public class MovieDAO {

    private final DatabaseManager db;

    public MovieDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new movie into the database.
     * @return the auto-generated movie ID
     */
    public int insert(Movie movie) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO movie (title, genre, duration_minutes, rating, director, release_date) " +
            "VALUES ('%s', '%s', %d, '%s', '%s', '%s')",
            DatabaseManager.escapeString(movie.getTitle()),
            DatabaseManager.escapeString(movie.getGenre()),
            movie.getDurationMinutes(),
            DatabaseManager.escapeString(movie.getRating()),
            DatabaseManager.escapeString(movie.getDirector()),
            DatabaseManager.escapeString(movie.getReleaseDate())
        );
        return db.executeInsert(sql);
    }

    /**
     * Retrieves a movie by its ID.
     */
    public Movie findById(int movieId) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM movie WHERE movie_id = %d", movieId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToMovie(results.get(0));
    }

    /**
     * Retrieves all movies from the database.
     */
    public List<Movie> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM movie ORDER BY movie_id";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Movie> movies = new ArrayList<>();
        for (Map<String, String> row : results) {
            movies.add(mapToMovie(row));
        }
        return movies;
    }

    /**
     * Searches movies by title (partial match).
     */
    public List<Movie> searchByTitle(String keyword) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM movie WHERE title LIKE '%%%s%%' ORDER BY title",
            DatabaseManager.escapeString(keyword)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Movie> movies = new ArrayList<>();
        for (Map<String, String> row : results) {
            movies.add(mapToMovie(row));
        }
        return movies;
    }

    /**
     * Searches movies by genre.
     */
    public List<Movie> searchByGenre(String genre) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM movie WHERE genre LIKE '%%%s%%' ORDER BY title",
            DatabaseManager.escapeString(genre)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Movie> movies = new ArrayList<>();
        for (Map<String, String> row : results) {
            movies.add(mapToMovie(row));
        }
        return movies;
    }

    /**
     * Updates an existing movie record.
     */
    public boolean update(Movie movie) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE movie SET title='%s', genre='%s', duration_minutes=%d, " +
            "rating='%s', director='%s', release_date='%s' WHERE movie_id=%d",
            DatabaseManager.escapeString(movie.getTitle()),
            DatabaseManager.escapeString(movie.getGenre()),
            movie.getDurationMinutes(),
            DatabaseManager.escapeString(movie.getRating()),
            DatabaseManager.escapeString(movie.getDirector()),
            DatabaseManager.escapeString(movie.getReleaseDate()),
            movie.getMovieId()
        );
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Deletes a movie by ID.
     */
    public boolean delete(int movieId) throws DatabaseConnectionException {
        String sql = String.format("DELETE FROM movie WHERE movie_id = %d", movieId);
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Gets the total count of movies.
     */
    public int getCount() throws DatabaseConnectionException {
        return db.getRowCount("movie");
    }

    /**
     * Maps a database row to a Movie object.
     */
    private Movie mapToMovie(Map<String, String> row) {
        Movie movie = new Movie();
        movie.setMovieId(parseIntSafe(row.get("movie_id")));
        movie.setTitle(row.get("title"));
        movie.setGenre(row.get("genre"));
        movie.setDurationMinutes(parseIntSafe(row.get("duration_minutes")));
        movie.setRating(row.get("rating"));
        movie.setDirector(row.get("director"));
        movie.setReleaseDate(row.get("release_date"));
        return movie;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
