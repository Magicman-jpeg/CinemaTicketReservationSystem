package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Admin;

import java.util.*;

/**
 * Data Access Object for Admin entity.
 * Provides CRUD operations for the admin table.
 */
public class AdminDAO {

    private final DatabaseManager db;

    public AdminDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new admin into the database.
     * @return the auto-generated admin ID
     */
    public int insert(Admin admin) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO admin (username, password, full_name, email, department, access_level) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
            DatabaseManager.escapeString(admin.getUsername()),
            DatabaseManager.escapeString(admin.getPassword()),
            DatabaseManager.escapeString(admin.getFullName()),
            DatabaseManager.escapeString(admin.getEmail()),
            DatabaseManager.escapeString(admin.getDepartment()),
            DatabaseManager.escapeString(admin.getAccessLevel())
        );
        return db.executeInsert(sql);
    }

    /**
     * Finds an admin by username and password (for login).
     */
    public Admin findByCredentials(String username, String password) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM admin WHERE username='%s' AND password='%s'",
            DatabaseManager.escapeString(username),
            DatabaseManager.escapeString(password)
        );
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToAdmin(results.get(0));
    }

    /**
     * Retrieves an admin by ID.
     */
    public Admin findById(int adminId) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM admin WHERE admin_id = %d", adminId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToAdmin(results.get(0));
    }

    /**
     * Retrieves all admins.
     */
    public List<Admin> findAll() throws DatabaseConnectionException {
        String sql = "SELECT * FROM admin ORDER BY admin_id";
        List<Map<String, String>> results = db.executeQuery(sql);
        List<Admin> admins = new ArrayList<>();
        for (Map<String, String> row : results) {
            admins.add(mapToAdmin(row));
        }
        return admins;
    }

    /**
     * Updates an admin record.
     */
    public boolean update(Admin admin) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE admin SET username='%s', password='%s', full_name='%s', " +
            "email='%s', department='%s', access_level='%s' WHERE admin_id=%d",
            DatabaseManager.escapeString(admin.getUsername()),
            DatabaseManager.escapeString(admin.getPassword()),
            DatabaseManager.escapeString(admin.getFullName()),
            DatabaseManager.escapeString(admin.getEmail()),
            DatabaseManager.escapeString(admin.getDepartment()),
            DatabaseManager.escapeString(admin.getAccessLevel()),
            admin.getUserId()
        );
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Deletes an admin by ID.
     */
    public boolean delete(int adminId) throws DatabaseConnectionException {
        String sql = String.format("DELETE FROM admin WHERE admin_id = %d", adminId);
        db.executeUpdate(sql);
        return true;
    }

    /**
     * Gets the total count of admins.
     */
    public int getCount() throws DatabaseConnectionException {
        return db.getRowCount("admin");
    }

    /**
     * Maps a database row to an Admin object.
     */
    private Admin mapToAdmin(Map<String, String> row) {
        Admin admin = new Admin();
        admin.setUserId(parseIntSafe(row.get("admin_id")));
        admin.setUsername(row.get("username"));
        admin.setPassword(row.get("password"));
        admin.setFullName(row.get("full_name"));
        admin.setEmail(row.get("email"));
        admin.setDepartment(row.get("department"));
        admin.setAccessLevel(row.get("access_level"));
        return admin;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
