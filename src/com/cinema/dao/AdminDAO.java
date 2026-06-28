package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.Admin;
import java.util.*;

/**
 * Data Access Object for Admin entity.
 */
public class AdminDAO {

    private final DatabaseManager db;

    public AdminDAO() { this.db = DatabaseManager.getInstance(); }

    public Admin findByCredentials(String username, String password) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT * FROM admin WHERE admin_username='%s' AND admin_pass='%s'",
            DatabaseManager.escapeString(username), DatabaseManager.escapeString(password));
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToAdmin(results.get(0));
    }

    public Admin findById(int adminId) throws DatabaseConnectionException {
        String sql = String.format("SELECT * FROM admin WHERE admin_id = %d", adminId);
        List<Map<String, String>> results = db.executeQuery(sql);
        if (results.isEmpty()) return null;
        return mapToAdmin(results.get(0));
    }

    public List<Admin> findAll() throws DatabaseConnectionException {
        List<Map<String, String>> results = db.executeQuery("SELECT * FROM admin ORDER BY admin_id");
        List<Admin> admins = new ArrayList<>();
        for (Map<String, String> row : results) admins.add(mapToAdmin(row));
        return admins;
    }

    public int insert(Admin admin) throws DatabaseConnectionException {
        String sql = String.format(
            "INSERT INTO admin (admin_id, role, admin_name, admin_username, admin_pass) " +
            "VALUES (%d, '%s', '%s', '%s', '%s')",
            admin.getAdminId(), DatabaseManager.escapeString(admin.getAdminRole()),
            DatabaseManager.escapeString(admin.getAdminName()),
            DatabaseManager.escapeString(admin.getUsername()),
            DatabaseManager.escapeString(admin.getPassword()));
        return db.executeInsert(sql);
    }

    public boolean update(Admin admin) throws DatabaseConnectionException {
        String sql = String.format(
            "UPDATE admin SET role='%s', admin_name='%s', admin_username='%s', admin_pass='%s' WHERE admin_id=%d",
            DatabaseManager.escapeString(admin.getAdminRole()),
            DatabaseManager.escapeString(admin.getAdminName()),
            DatabaseManager.escapeString(admin.getUsername()),
            DatabaseManager.escapeString(admin.getPassword()), admin.getAdminId());
        db.executeUpdate(sql);
        return true;
    }

    public boolean delete(int adminId) throws DatabaseConnectionException {
        db.executeUpdate(String.format("DELETE FROM admin WHERE admin_id = %d", adminId));
        return true;
    }

    public int getCount() throws DatabaseConnectionException { return db.getRowCount("admin"); }

    private Admin mapToAdmin(Map<String, String> row) {
        Admin a = new Admin();
        a.setAdminId(parseInt(row.get("admin_id")));
        a.setAdminRole(row.get("role"));
        a.setAdminName(row.get("admin_name"));
        a.setUsername(row.get("admin_username"));
        a.setPassword(row.get("admin_pass"));
        return a;
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
}
