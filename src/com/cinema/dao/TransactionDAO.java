package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Transaction;

import java.util.*;

/**
 * Data Access Object for Transaction entity.
 * Provides CRUD operations for the transaction table.
 */
public class TransactionDAO {

    private final DatabaseManager db;

    public TransactionDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new transaction into the database.
     */
    public int insert(Transaction txn) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO \"transaction\" (customer_id, screening_id, seat_id, seat_label, " +
            "transaction_date, transaction_time, amount_paid, payment_method, status) " +
            "VALUES (%d, %d, %d, '%s', '%s', '%s', %.2f, '%s', '%s')",
            txn.getCustomerId(),
            txn.getScreeningId(),
            txn.getSeatId(),
            DatabaseManager.escapeString(txn.getSeatLabel()),
            DatabaseManager.escapeString(txn.getTransactionDate()),
            DatabaseManager.escapeString(txn.getTransactionTime()),
            txn.getAmountPaid(),
            DatabaseManager.escapeString(txn.getPaymentMethod()),
            DatabaseManager.escapeString(txn.getStatus())
        );
        return db.executeInsert(sql);
    }


    /**
     * Retrieves a transaction by ID.
     */
    public Transaction findById(int transactionId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM \"transaction\" WHERE transaction_id = %d", transactionId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToTransaction(results.get(0));
    }

    /**
     * Retrieves all transactions.
     */
    public List<Transaction> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM \"transaction\" ORDER BY transaction_id DESC";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Transaction> transactions = new ArrayList<>();
        for (Map<String, String> row : results) {
            transactions.add(mapToTransaction(row));
        }
        return transactions;
    }

    /**
     * Retrieves all transactions for a specific customer.
     */
    public List<Transaction> findByCustomerId(int customerId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM \"transaction\" WHERE customer_id = %d ORDER BY transaction_date DESC",
            customerId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Transaction> transactions = new ArrayList<>();
        for (Map<String, String> row : results) {
            transactions.add(mapToTransaction(row));
        }
        return transactions;
    }

    /**
     * Retrieves all transactions for a specific screening.
     */
    public List<Transaction> findByScreeningId(int screeningId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM \"transaction\" WHERE screening_id = %d ORDER BY transaction_id",
            screeningId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Transaction> transactions = new ArrayList<>();
        for (Map<String, String> row : results) {
            transactions.add(mapToTransaction(row));
        }
        return transactions;
    }


    /**
     * Checks if a seat is already booked for a screening.
     */
    public boolean isSeatBooked(int screeningId, int seatId) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT COUNT(*) as cnt FROM \"transaction\" " +
            "WHERE screening_id=%d AND seat_id=%d AND status='CONFIRMED'",
            screeningId, seatId
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        return !results.isEmpty() && Integer.parseInt(results.get(0).get("cnt")) > 0;
    }

    /**
     * Updates a transaction status.
     */
    public boolean updateStatus(int transactionId, String status) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE \"transaction\" SET status='%s' WHERE transaction_id=%d",
            DatabaseManager.escapeString(status), transactionId
        );
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Cancels a transaction.
     */
    public boolean cancel(int transactionId) throws DatabaseConnectionException {
        return updateStatus(transactionId, Transaction.STATUS_CANCELLED);
    }

    /**
     * Deletes a transaction by ID.
     */
    public boolean delete(int transactionId) throws DatabaseConnectionException {
        String sql = String.format(
            "DELETE FROM \"transaction\" WHERE transaction_id = %d", transactionId);
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Gets the total revenue from confirmed transactions.
     */
    public double getTotalRevenue() throws DatabaseConnectionException {
        String sql = "SELECT COALESCE(SUM(amount_paid), 0) as total FROM \"transaction\" WHERE status='CONFIRMED'";
        List<Map<String, String>> results = db.executeQuery(sql);
        if (!results.isEmpty()) {
            try {
                return Double.parseDouble(results.get(0).get("total"));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }


    /**
     * Gets revenue by date.
     */
    public List<Map<String, String>> getRevenueByDate() throws DatabaseConnectionException {
        String sql = "SELECT transaction_date, SUM(amount_paid) as daily_revenue, COUNT(*) as ticket_count " +
                     "FROM \"transaction\" WHERE status='CONFIRMED' " +
                     "GROUP BY transaction_date ORDER BY transaction_date DESC";
        return db.executeQuery(sql);
    }

    /**
     * Gets the most popular movies by ticket sales.
     */
    public List<Map<String, String>> getPopularMovies() throws DatabaseConnectionException {
        String sql = "SELECT m.title, COUNT(t.transaction_id) as tickets_sold, " +
                     "SUM(t.amount_paid) as total_revenue " +
                     "FROM \"transaction\" t " +
                     "JOIN screenings s ON t.screening_id = s.screening_id " +
                     "JOIN movie m ON s.movie_id = m.movie_id " +
                     "WHERE t.status='CONFIRMED' " +
                     "GROUP BY m.movie_id ORDER BY tickets_sold DESC";
        return db.executeQuery(sql);
    }

    /**
     * Gets total count of transactions.
     */
    public int getCount() throws DatabaseConnectionException {
        return db.getRowCount("\"transaction\"");
    }

    /**
     * Maps a database row to a Transaction object.
     */
    private Transaction mapToTransaction(Map<String, String> row) {
        Transaction txn = new Transaction();
        txn.setTransactionId(parseIntSafe(row.get("transaction_id")));
        txn.setCustomerId(parseIntSafe(row.get("customer_id")));
        txn.setScreeningId(parseIntSafe(row.get("screening_id")));
        txn.setSeatId(parseIntSafe(row.get("seat_id")));
        txn.setSeatLabel(row.get("seat_label"));
        txn.setTransactionDate(row.get("transaction_date"));
        txn.setTransactionTime(row.get("transaction_time"));
        txn.setAmountPaid(parseDoubleSafe(row.get("amount_paid")));
        txn.setPaymentMethod(row.get("payment_method"));
        txn.setStatus(row.get("status"));
        return txn;
    }

    private int parseIntSafe(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }

    private double parseDoubleSafe(String value) {
        try { return Double.parseDouble(value); }
        catch (Exception e) { return 0.0; }
    }
}
