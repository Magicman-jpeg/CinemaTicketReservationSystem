package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Screening;

import java.util.*;

/**
 * Data Access Object for Screening entity.
 * Provides CRUD operations for the screenings table.
 */
public class ScreeningDAO {

    private final DatabaseManager db;

    public ScreeningDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new screening into the database.
     * @return the auto-generated screening ID
     */
    public int insert(Screening screening) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO screenings (movie_id, screen_date, screen_time, hall_number, ticket_price) " +
            "VALUES (%d, '%s', '%s', %d, %.2f)",
            screening.getMovieId(),
            DatabaseManager.escapeString(screening.getScreenDate()),
            DatabaseManager.escapeString(screening.getScreenTime()),
            screening.getHallNumber(),
            screening.getTicketPrice()
        );
        return db.executeInsert(sql);
    }

    /**
     * Retrieves a screening by ID.
     */
    public Screening findById(int screeningId) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM screenings WHERE screening_id = %d", screeningId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToScreening(results.get(0));
    }

    /**
     * Retrieves all screenings.
     */
    public List<Screening> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM screenings ORDER BY screen_date, screen_time";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Screening> screenings = new ArrayList<>();
        for (Map<String, String> row : results) {
            screenings.add(mapToScreening(row));
        }
        return screenings;
    }

    /**
     * Retrieves all screenings for a specific movie.
     */
    public List<Screening> findByMovieId(int movieId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM screenings WHERE movie_id = %d ORDER BY screen_date, screen_time",
            movieId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Screening> screenings = new ArrayList<>();
        for (Map<String, String> row : results) {
            screenings.add(mapToScreening(row));
        }
        return screenings;
    }

    /**
     * Retrieves screenings for a specific date.
     */
    public List<Screening> findByDate(String date) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM screenings WHERE screen_date = '%s' ORDER BY screen_time",
            DatabaseManager.escapeString(date)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Screening> screenings = new ArrayList<>();
        for (Map<String, String> row : results) {
            screenings.add(mapToScreening(row));
        }
        return screenings;
    }

    /**
     * Updates a screening record.
     */
    public boolean update(Screening screening) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE screenings SET movie_id=%d, screen_date='%s', screen_time='%s', " +
            "hall_number=%d, ticket_price=%.2f WHERE screening_id=%d",
            screening.getMovieId(),
            DatabaseManager.escapeString(screening.getScreenDate()),
            DatabaseManager.escapeString(screening.getScreenTime()),
            screening.getHallNumber(),
            screening.getTicketPrice(),
            screening.getScreeningId()
        );
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Deletes a screening by ID.
     */
    public boolean delete(int screeningId) throws DatabaseConnectionException {
        String sql = String.format("DELETE FROM screenings WHERE screening_id = %d", screeningId);
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Gets the total count of screenings.
     */
    public int getCount() throws DatabaseConnectionException {
        return db.getRowCount("screenings");
    }

    /**
     * Maps a database row to a Screening object.
     */
    private Screening mapToScreening(Map<String, String> row) {
        Screening screening = new Screening();
        screening.setScreeningId(parseIntSafe(row.get("screening_id")));
        screening.setMovieId(parseIntSafe(row.get("movie_id")));
        screening.setScreenDate(row.get("screen_date"));
        screening.setScreenTime(row.get("screen_time"));
        screening.setHallNumber(parseIntSafe(row.get("hall_number")));
        screening.setTicketPrice(parseDoubleSafe(row.get("ticket_price")));
        return screening;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
