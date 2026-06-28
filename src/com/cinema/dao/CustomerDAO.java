package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Customer;

import java.util.*;

/**
 * Data Access Object for Customer entity.
 * Provides CRUD operations for the customer table.
 */
public class CustomerDAO {

    private final DatabaseManager db;

    public CustomerDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new customer into the database.
     * @return the auto-generated customer ID
     */
    public int insert(Customer customer) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO customer (username, password, full_name, email, phone_number, " +
            "membership_type, registration_date) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            DatabaseManager.escapeString(customer.getUsername()),
            DatabaseManager.escapeString(customer.getPassword()),
            DatabaseManager.escapeString(customer.getFullName()),
            DatabaseManager.escapeString(customer.getEmail()),
            DatabaseManager.escapeString(customer.getPhoneNumber()),
            DatabaseManager.escapeString(customer.getMembershipType()),
            DatabaseManager.escapeString(customer.getRegistrationDate())
        );
        return db.executeInsert(sql);
    }

    /**
     * Finds a customer by username and password (for login).
     */
    public Customer findByCredentials(String username, String password) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM customer WHERE username='%s' AND password='%s'",
            DatabaseManager.escapeString(username),
            DatabaseManager.escapeString(password)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToCustomer(results.get(0));
    }

    /**
     * Retrieves a customer by ID.
     */
    public Customer findById(int customerId) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM customer WHERE customer_id = %d", customerId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToCustomer(results.get(0));
    }

    /**
     * Retrieves all customers.
     */
    public List<Customer> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM customer ORDER BY customer_id";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Customer> customers = new ArrayList<>();
        for (Map<String, String> row : results) {
            customers.add(mapToCustomer(row));
        }
        return customers;
    }

    /**
     * Searches customers by name (partial match).
     */
    public List<Customer> searchByName(String keyword) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM customer WHERE full_name LIKE '%%%s%%' ORDER BY full_name",
            DatabaseManager.escapeString(keyword)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Customer> customers = new ArrayList<>();
        for (Map<String, String> row : results) {
            customers.add(mapToCustomer(row));
        }
        return customers;
    }

    /**
     * Checks if a username already exists.
     */
    public boolean usernameExists(String username) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT COUNT(*) as cnt FROM customer WHERE username='%s'",
            DatabaseManager.escapeString(username)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        return !results.isEmpty() && Integer.parseInt(results.get(0).get("cnt")) > 0;
    }

    /**
     * Updates a customer record.
     */
    public boolean update(Customer customer) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE customer SET username='%s', password='%s', full_name='%s', " +
            "email='%s', phone_number='%s', membership_type='%s' WHERE customer_id=%d",
            DatabaseManager.escapeString(customer.getUsername()),
            DatabaseManager.escapeString(customer.getPassword()),
            DatabaseManager.escapeString(customer.getFullName()),
            DatabaseManager.escapeString(customer.getEmail()),
            DatabaseManager.escapeString(customer.getPhoneNumber()),
            DatabaseManager.escapeString(customer.getMembershipType()),
            customer.getUserId()
        );
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Deletes a customer by ID.
     */
    public boolean delete(int customerId) throws DatabaseConnectionException {
        String sql = String.format("DELETE FROM customer WHERE customer_id = %d", customerId);
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Gets the total count of customers.
     */
    public int getCount() throws DatabaseConnectionException {
        return db.getRowCount("customer");
    }

    /**
     * Maps a database row to a Customer object.
     */
    private Customer mapToCustomer(Map<String, String> row) {
        Customer customer = new Customer();
        customer.setUserId(parseIntSafe(row.get("customer_id")));
        customer.setUsername(row.get("username"));
        customer.setPassword(row.get("password"));
        customer.setFullName(row.get("full_name"));
        customer.setEmail(row.get("email"));
        customer.setPhoneNumber(row.get("phone_number"));
        customer.setMembershipType(row.get("membership_type"));
        customer.setRegistrationDate(row.get("registration_date"));
        return customer;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
