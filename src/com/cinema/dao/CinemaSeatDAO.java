package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.CinemaSeat;
import java.util.*;

/**
 * Data Access Object for CinemaSeat entity.
 * Seats are static (A1-J10). Availability is checked via transaction table.
 */
public class CinemaSeatDAO {

    private final DatabaseManager db;

    public CinemaSeatDAO() { this.db = DatabaseManager.getInstance(); }

    public List<CinemaSeat> findAll() throws DatabaseConnectionException {
        List<Map<String, String>> results = db.executeQuery(
            "SELECT * FROM cinema_seat ORDER BY row, col");
        List<CinemaSeat> seats = new ArrayList<>();
        for (Map<String, String> row : results) seats.add(mapToSeat(row));
        return seats;
    }

    public CinemaSeat findBySeatNo(String seatNo) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM cinema_seat WHERE seat_no = '%s'",
                DatabaseManager.escapeString(seatNo));
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToSeat(results.get(0));
    }

    /**
     * Gets booked seats for a screening (from transaction table).
     */
    public Set<String> getBookedSeats(String screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT seat_no FROM \"transaction\" WHERE screening_id='%s' AND status='CONFIRMED' AND seat_no IS NOT NULL",
            DatabaseManager.escapeString(screeningId));
        List<Map<String, String>> results = db.executeQuery(sql);
        Set<String> booked = new HashSet<>();
        for (Map<String, String> row : results) {
            String sn = row.get("seat_no");
            if (sn != null && !sn.isEmpty()) booked.add(sn);
        }
        return booked;
    }

    /**
     * Gets count of available seats for a screening.
     */
    public int getAvailableCount(String screeningId) throws DatabaseConnectionException {
        Set<String> booked = getBookedSeats(screeningId);
        return 100 - booked.size(); // 100 total seats
    }

    public int getCount() throws DatabaseConnectionException { return db.getRowCount("cinema_seat"); }

    private CinemaSeat mapToSeat(Map<String, String> row) {
        CinemaSeat s = new CinemaSeat();
        s.setSeatNo(row.get("seat_no"));
        s.setRow(row.get("row"));
        s.setCol(parseInt(row.get("col")));
        return s;
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
}
