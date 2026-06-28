package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.CinemaSeat;

import java.util.*;

/**
 * Data Access Object for CinemaSeat entity.
 * Provides CRUD operations for the cinema_seat table.
 */
public class CinemaSeatDAO {

    private final DatabaseManager db;

    public CinemaSeatDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new seat into the database.
     */
    public int insert(CinemaSeat seat) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO cinema_seat (screening_id, seat_row, seat_column, status) " +
            "VALUES (%d, '%s', %d, '%s')",
            seat.getScreeningId(),
            DatabaseManager.escapeString(seat.getSeatRow()),
            seat.getSeatColumn(),
            DatabaseManager.escapeString(seat.getStatus())
        );
        return db.executeInsert(sql);
    }

    /**
     * Retrieves a seat by its ID.
     */
    public CinemaSeat findById(int seatId) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM cinema_seat WHERE seat_id = %d", seatId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToSeat(results.get(0));
    }


    /**
     * Finds a seat by screening, row, and column.
     */
    public CinemaSeat findByPosition(int screeningId, String row, int column) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM cinema_seat WHERE screening_id=%d AND seat_row='%s' AND seat_column=%d",
            screeningId, DatabaseManager.escapeString(row), column
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToSeat(results.get(0));
    }

    /**
     * Retrieves all seats for a screening.
     */
    public List<CinemaSeat> findByScreeningId(int screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM cinema_seat WHERE screening_id = %d ORDER BY seat_row, seat_column",
            screeningId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<CinemaSeat> seats = new ArrayList<>();
        for (Map<String, String> row : results) {
            seats.add(mapToSeat(row));
        }
        return seats;
    }

    /**
     * Retrieves all available seats for a screening.
     */
    public List<CinemaSeat> findAvailableByScreeningId(int screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM cinema_seat WHERE screening_id = %d AND status = 'AVAILABLE' " +
            "ORDER BY seat_row, seat_column",
            screeningId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<CinemaSeat> seats = new ArrayList<>();
        for (Map<String, String> row : results) {
            seats.add(mapToSeat(row));
        }
        return seats;
    }


    /**
     * Updates seat status (AVAILABLE, RESERVED, OCCUPIED).
     */
    public boolean updateStatus(int seatId, String status) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE cinema_seat SET status='%s' WHERE seat_id=%d",
            DatabaseManager.escapeString(status), seatId
        );
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Reserves a seat (sets status to RESERVED).
     */
    public boolean reserveSeat(int seatId) throws DatabaseConnectionException {
        return updateStatus(seatId, CinemaSeat.STATUS_RESERVED);
    }

    /**
     * Releases a seat (sets status to AVAILABLE).
     */
    public boolean releaseSeat(int seatId) throws DatabaseConnectionException {
        return updateStatus(seatId, CinemaSeat.STATUS_AVAILABLE);
    }

    /**
     * Generates all seats for a screening (rows A-K, columns 1-10).
     */
    public void generateSeatsForScreening(int screeningId) throws DatabaseConnectionException {
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
        StringBuilder sql = new StringBuilder();
        for (String row : rows) {
            for (int col = 1; col <= 10; col++) {
                sql.append(String.format(
                    "INSERT OR IGNORE INTO cinema_seat (screening_id, seat_row, seat_column, status) " +
                    "VALUES (%d, '%s', %d, 'AVAILABLE');\n",
                    screeningId, row, col
                ));
            }
        }
        db.executeUpdate(sql.toString());
    }


    /**
     * Gets count of available seats for a screening.
     */
    public int getAvailableCount(int screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT COUNT(*) as cnt FROM cinema_seat WHERE screening_id=%d AND status='AVAILABLE'",
            screeningId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        if (!results.isEmpty()) {
            try {
                return Integer.parseInt(results.get(0).get("cnt"));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * Gets total count of seats.
     */
    public int getCount() throws DatabaseConnectionException {
        return db.getRowCount("cinema_seat");
    }

    /**
     * Deletes all seats for a screening.
     */
    public boolean deleteByScreeningId(int screeningId) throws DatabaseConnectionException {
        String sql = String.format("DELETE FROM cinema_seat WHERE screening_id = %d", screeningId);
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Maps a database row to a CinemaSeat object.
     */
    private CinemaSeat mapToSeat(Map<String, String> row) {
        CinemaSeat seat = new CinemaSeat();
        seat.setSeatId(parseIntSafe(row.get("seat_id")));
        seat.setScreeningId(parseIntSafe(row.get("screening_id")));
        seat.setSeatRow(row.get("seat_row"));
        seat.setSeatColumn(parseIntSafe(row.get("seat_column")));
        seat.setStatus(row.get("status"));
        return seat;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
