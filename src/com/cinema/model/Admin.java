package com.cinema.model;

/**
 * Represents an Admin user (from admin table in Excel).
 * Fields: admin_id, role, admin_name, admin_username, admin_pass
 */
public class Admin extends User {

    private int adminId;
    private String adminRole; // Manager, Assistant Manager, Box Office Staff, Admin Staff
    private String adminName;

    public Admin() { super(); setRole("ADMIN"); }

    public Admin(int adminId, String adminRole, String adminName,
                 String username, String password) {
        super(username, password, "ADMIN");
        this.adminId = adminId;
        this.adminRole = adminRole;
        this.adminName = adminName;
    }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public String getAdminRole() { return adminRole; }
    public void setAdminRole(String adminRole) { this.adminRole = adminRole; }
    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    @Override
    public String displayInfo() {
        return String.format(
            "=== ADMIN PROFILE ===\n" +
            "ID:       %d\n" +
            "Name:     %s\n" +
            "Role:     %s\n" +
            "Username: %s\n" +
            "=====================",
            adminId, adminName, adminRole, getUsername());
    }

    @Override
    public String toString() {
        return String.format("| %-6d | %-20s | %-20s | %-18s |",
                adminId, adminName, adminRole, getUsername());
    }

    public static String getTableHeader() {
        return String.format("| %-6s | %-20s | %-20s | %-18s |",
                "ID", "Name", "Role", "Username");
    }

    public static String getTableDivider() {
        return "+--------+----------------------+----------------------+--------------------+";
    }
}
