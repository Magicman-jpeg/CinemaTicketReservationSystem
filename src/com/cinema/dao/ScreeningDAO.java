package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Screening;
import java.util.*;

/**
 * Data Access Object for Screening entity.
 */
public class ScreeningDAO {

    private final DatabaseManager db;

    public ScreeningDAO() { this.db = DatabaseManager.getInstance(); }

    public List<Screening> findAll() throws DatabaseConnectionException {
        String sql = "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price " +
                     "FROM screenings s " +
                     "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
                     "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
                     "ORDER BY s.screening_date, s.time_slot";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Screening> screenings = new ArrayList<>();
        for (Map<String, String> row : results) screenings.add(mapToScreening(row));
        return screenings;
    }

    public Screening findById(String screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price " +
            "FROM screenings s " +
            "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
            "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
            "WHERE s.screening_id = '%s'", DatabaseManager.escapeString(screeningId));
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToScreening(results.get(0));
    }

    public List<Screening> findByMovieId(int movieId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price " +
            "FROM screenings s " +
            "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
            "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
            "WHERE s.movie_id = %d ORDER BY s.screening_date, s.time_slot", movieId);
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Screening> screenings = new ArrayList<>();
        for (Map<String, String> row : results) screenings.add(mapToScreening(row));
        return screenings;
    }

    public List<Screening> findByDay(String day) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price " +
            "FROM screenings s " +
            "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
            "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
            "WHERE s.screening_day = '%s' ORDER BY s.time_slot",
            DatabaseManager.escapeString(day));
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Screening> screenings = new ArrayList<>();
        for (Map<String, String> row : results) screenings.add(mapToScreening(row));
        return screenings;
    }

    public int insert(Screening s) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO screenings (screening_id, screening_day, screening_date, time_slot, seat_type_id, movie_id, cinema_no) " +
            "VALUES ('%s', '%s', '%s', '%s', %d, %d, %d)",
            DatabaseManager.escapeString(s.getScreeningId()), DatabaseManager.escapeString(s.getScreeningDay()),
            DatabaseManager.escapeString(s.getScreeningDate()), DatabaseManager.escapeString(s.getTimeSlot()),
            s.getSeatTypeId(), s.getMovieId(), s.getCinemaNo());
        return db.executeInsert(sql);
    }

    public boolean update(Screening s) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE screenings SET screening_day='%s', screening_date='%s', time_slot='%s', " +
            "seat_type_id=%d, movie_id=%d, cinema_no=%d WHERE screening_id='%s'",
            DatabaseManager.escapeString(s.getScreeningDay()), DatabaseManager.escapeString(s.getScreeningDate()),
            DatabaseManager.escapeString(s.getTimeSlot()), s.getSeatTypeId(), s.getMovieId(),
            s.getCinemaNo(), DatabaseManager.escapeString(s.getScreeningId()));
        db.executeUpdate(sql);
        return true;
    }

    public boolean delete(String screeningId) throws DatabaseConnectionException {
        db.executeUpdate(String.format("DELETE FROM screenings WHERE screening_id = '%s'",
                DatabaseManager.escapeString(screeningId)));
        return true;
    }

    public int getCount() throws DatabaseConnectionException { return db.getRowCount("screenings"); }

    private Screening mapToScreening(Map<String, String> row) {
        Screening s = new Screening();
        s.setScreeningId(row.get("screening_id"));
        s.setScreeningDay(row.get("screening_day"));
        s.setScreeningDate(row.get("screening_date"));
        s.setTimeSlot(row.get("time_slot"));
        s.setSeatTypeId(parseInt(row.get("seat_type_id")));
        s.setMovieId(parseInt(row.get("movie_id")));
        s.setCinemaNo(parseInt(row.get("cinema_no")));
        s.setMovieTitle(row.get("movie_title"));
        s.setSeatTypeName(row.get("seat_type"));
        s.setTicketPrice(parseDouble(row.get("ticket_price")));
        return s;
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
    private double parseDouble(String v) { try { return Double.parseDouble(v); } catch (Exception e) { return 0.0; } }
}
