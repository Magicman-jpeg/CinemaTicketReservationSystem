package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Transaction;
import java.util.*;

/**
 * Data Access Object for Transaction entity.
 */
public class TransactionDAO {

    private final DatabaseManager db;

    public TransactionDAO() { this.db = DatabaseManager.getInstance(); }

    public int insert(Transaction t) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO \"transaction\" (transaction_id, transaction_date, transaction_time, " +
            "customer_no, seat_no, screening_id, movie_id, seat_type_id, reservation_type, " +
            "admin_id, booking_fee, ticket_price, discount_type, discount_amount, " +
            "payment_method, total_payment, status) VALUES " +
            "('%s','%s','%s',%d,%s,'%s',%d,%d,'%s',%s,%.2f,%.2f,'%s',%.2f,%s,%.2f,'%s')",
            DatabaseManager.escapeString(t.getTransactionId()),
            DatabaseManager.escapeString(t.getTransactionDate()),
            DatabaseManager.escapeString(t.getTransactionTime()),
            t.getCustomerNo(),
            t.getSeatNo() != null ? "'" + DatabaseManager.escapeString(t.getSeatNo()) + "'" : "NULL",
            DatabaseManager.escapeString(t.getScreeningId()),
            t.getMovieId(), t.getSeatTypeId(),
            DatabaseManager.escapeString(t.getReservationType()),
            t.getAdminId() != null ? "'" + DatabaseManager.escapeString(t.getAdminId()) + "'" : "NULL",
            t.getBookingFee(), t.getTicketPrice(),
            DatabaseManager.escapeString(t.getDiscountType()), t.getDiscountAmount(),
            t.getPaymentMethod() != null && !t.getPaymentMethod().equals("N/A") ?
                "'" + DatabaseManager.escapeString(t.getPaymentMethod()) + "'" : "NULL",
            t.getTotalPayment(), DatabaseManager.escapeString(t.getStatus()));
        return db.executeInsert(sql);
    }


    public Transaction findById(String transactionId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM \"transaction\" WHERE transaction_id = '%s'",
            DatabaseManager.escapeString(transactionId));
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToTransaction(results.get(0));
    }

    public List<Transaction> findAll() throws DatabaseConnectionException {
        List<Map<String, String>> results = db.executeQuery(
            "SELECT * FROM \"transaction\" ORDER BY transaction_date DESC, transaction_time DESC");
        List<Transaction> txns = new ArrayList<>();
        for (Map<String, String> row : results) txns.add(mapToTransaction(row));
        return txns;
    }

    public List<Transaction> findByCustomerNo(int customerNo) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM \"transaction\" WHERE customer_no = %d ORDER BY transaction_date DESC", customerNo);
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Transaction> txns = new ArrayList<>();
        for (Map<String, String> row : results) txns.add(mapToTransaction(row));
        return txns;
    }

    public List<Transaction> findByScreeningId(String screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM \"transaction\" WHERE screening_id = '%s' AND status='CONFIRMED'",
            DatabaseManager.escapeString(screeningId));
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Transaction> txns = new ArrayList<>();
        for (Map<String, String> row : results) txns.add(mapToTransaction(row));
        return txns;
    }


    public boolean isSeatBooked(String screeningId, String seatNo) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT COUNT(*) as cnt FROM \"transaction\" " +
            "WHERE screening_id='%s' AND seat_no='%s' AND status='CONFIRMED'",
            DatabaseManager.escapeString(screeningId), DatabaseManager.escapeString(seatNo));
        List<Map<String, String>> results = db.executeQuery(sql);
        return !results.isEmpty() && Integer.parseInt(results.get(0).get("cnt")) > 0;
    }

    public boolean cancel(String transactionId) throws DatabaseConnectionException {
        db.executeUpdate(String.format(
            "UPDATE \"transaction\" SET status='CANCELLED' WHERE transaction_id='%s'",
            DatabaseManager.escapeString(transactionId)));
        return true;
    }

    public boolean delete(String transactionId) throws DatabaseConnectionException {
        db.executeUpdate(String.format(
            "DELETE FROM \"transaction\" WHERE transaction_id = '%s'",
            DatabaseManager.escapeString(transactionId)));
        return true;
    }

    public double getTotalRevenue() throws DatabaseConnectionException {
        List<Map<String, String>> r = db.executeQuery(
            "SELECT COALESCE(SUM(total_payment), 0) as total FROM \"transaction\" WHERE status='CONFIRMED'");
        if (!r.isEmpty()) { try { return Double.parseDouble(r.get(0).get("total")); } catch (Exception e) {} }
        return 0.0;
    }

    public List<Map<String, String>> getRevenueByDate() throws DatabaseConnectionException {
        return db.executeQuery(
            "SELECT transaction_date, SUM(total_payment) as daily_revenue, COUNT(*) as ticket_count " +
            "FROM \"transaction\" WHERE status='CONFIRMED' GROUP BY transaction_date ORDER BY transaction_date DESC");
    }

    public List<Map<String, String>> getPopularMovies() throws DatabaseConnectionException {
        return db.executeQuery(
            "SELECT m.movie_title, COUNT(t.transaction_id) as tickets_sold, SUM(t.total_payment) as total_revenue " +
            "FROM \"transaction\" t JOIN movie m ON t.movie_id = m.movie_id " +
            "WHERE t.status='CONFIRMED' GROUP BY m.movie_id ORDER BY tickets_sold DESC");
    }

    public int getCount() throws DatabaseConnectionException { return db.getRowCount("\"transaction\""); }


    private Transaction mapToTransaction(Map<String, String> row) {
        Transaction t = new Transaction();
        t.setTransactionId(row.get("transaction_id"));
        t.setTransactionDate(row.get("transaction_date"));
        t.setTransactionTime(row.get("transaction_time"));
        t.setCustomerNo(parseInt(row.get("customer_no")));
        t.setSeatNo(row.get("seat_no"));
        t.setScreeningId(row.get("screening_id"));
        t.setMovieId(parseInt(row.get("movie_id")));
        t.setSeatTypeId(parseInt(row.get("seat_type_id")));
        t.setReservationType(row.get("reservation_type"));
        t.setAdminId(row.get("admin_id"));
        t.setBookingFee(parseDouble(row.get("booking_fee")));
        t.setTicketPrice(parseDouble(row.get("ticket_price")));
        t.setDiscountType(row.get("discount_type"));
        t.setDiscountAmount(parseDouble(row.get("discount_amount")));
        t.setPaymentMethod(row.get("payment_method"));
        t.setTotalPayment(parseDouble(row.get("total_payment")));
        t.setStatus(row.get("status"));
        return t;
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
    private double parseDouble(String v) { try { return Double.parseDouble(v); } catch (Exception e) { return 0.0; } }
}
