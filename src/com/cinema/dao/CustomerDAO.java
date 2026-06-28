package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Customer;
import java.util.*;

/**
 * Data Access Object for Customer entity.
 */
public class CustomerDAO {

    private final DatabaseManager db;

    public CustomerDAO() { this.db = DatabaseManager.getInstance(); }

    public Customer findByCredentials(String username, String password) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM customer WHERE customer_username='%s' AND customer_pass='%s'",
            DatabaseManager.escapeString(username), DatabaseManager.escapeString(password));
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToCustomer(results.get(0));
    }

    public Customer findById(int customerNo) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM customer WHERE customer_no = %d", customerNo);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToCustomer(results.get(0));
    }

    public List<Customer> findAll() throws DatabaseConnectionException {
        List<Map<String, String>> results = db.executeQuery("SELECT * FROM customer ORDER BY customer_no");
        List<Customer> customers = new ArrayList<>();
        for (Map<String, String> row : results) customers.add(mapToCustomer(row));
        return customers;
    }

    public List<Customer> searchByName(String keyword) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM customer WHERE name LIKE '%%%s%%' ORDER BY name",
            DatabaseManager.escapeString(keyword));
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Customer> customers = new ArrayList<>();
        for (Map<String, String> row : results) customers.add(mapToCustomer(row));
        return customers;
    }

    public boolean usernameExists(String username) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT COUNT(*) as cnt FROM customer WHERE customer_username='%s'",
            DatabaseManager.escapeString(username));
        List<Map<String, String>> results = db.executeQuery(sql);
        return !results.isEmpty() && Integer.parseInt(results.get(0).get("cnt")) > 0;
    }

    public int insert(Customer c) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO customer (name, age, email_address, app_user, customer_username, customer_pass, mobile_no) " +
            "VALUES ('%s', %d, '%s', '%s', %s, %s, '%s')",
            DatabaseManager.escapeString(c.getName()), c.getAge(),
            DatabaseManager.escapeString(c.getEmailAddress()), c.getAppUser(),
            c.getUsername() != null ? "'" + DatabaseManager.escapeString(c.getUsername()) + "'" : "NULL",
            c.getPassword() != null ? "'" + DatabaseManager.escapeString(c.getPassword()) + "'" : "NULL",
            DatabaseManager.escapeString(c.getMobileNo()));
        return db.executeInsert(sql);
    }

    public boolean update(Customer c) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE customer SET name='%s', age=%d, email_address='%s', app_user='%s', " +
            "customer_username=%s, customer_pass=%s, mobile_no='%s' WHERE customer_no=%d",
            DatabaseManager.escapeString(c.getName()), c.getAge(),
            DatabaseManager.escapeString(c.getEmailAddress()), c.getAppUser(),
            c.getUsername() != null ? "'" + DatabaseManager.escapeString(c.getUsername()) + "'" : "NULL",
            c.getPassword() != null ? "'" + DatabaseManager.escapeString(c.getPassword()) + "'" : "NULL",
            DatabaseManager.escapeString(c.getMobileNo()), c.getCustomerNo());
        db.executeUpdate(sql);
        return true;
    }

    public boolean delete(int customerNo) throws DatabaseConnectionException {
        db.executeUpdate(String.format("DELETE FROM customer WHERE customer_no = %d", customerNo));
        return true;
    }

    public int getCount() throws DatabaseConnectionException { return db.getRowCount("customer"); }

    public int getNextCustomerNo() throws DatabaseConnectionException {
        List<Map<String, String>> r = db.executeQuery("SELECT MAX(customer_no) as mx FROM customer");
        if (!r.isEmpty() && r.get(0).get("mx") != null) {
            try { return Integer.parseInt(r.get(0).get("mx")) + 1; } catch (Exception e) {}
        }
        return 1001;
    }

    private Customer mapToCustomer(Map<String, String> row) {
        Customer c = new Customer();
        c.setCustomerNo(parseInt(row.get("customer_no")));
        c.setName(row.get("name"));
        c.setAge(parseInt(row.get("age")));
        c.setEmailAddress(row.get("email_address"));
        c.setAppUser(row.get("app_user"));
        c.setUsername(row.get("customer_username"));
        c.setPassword(row.get("customer_pass"));
        c.setMobileNo(row.get("mobile_no"));
        return c;
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
}
