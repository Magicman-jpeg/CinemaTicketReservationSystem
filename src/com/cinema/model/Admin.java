package com.cinema.model;

/**
 * Represents an Admin user in the system.
 * Inherits from User (demonstrates inheritance).
 * Admins can manage movies, screenings, and view reports.
 */
public class Admin extends User {

    private String department;
    private String accessLevel; // FULL, LIMITED

    public static final String ROLE = "ADMIN";

    // Default constructor
    public Admin() {
        super();
        setRole(ROLE);
    }

    // Parameterized constructor
    public Admin(int userId, String username, String password,
                 String fullName, String email, String department, String accessLevel) {
        super(userId, username, password, fullName, email, ROLE);
        this.department = department;
        this.accessLevel = accessLevel;
    }

    // Constructor without ID
    public Admin(String username, String password, String fullName,
                 String email, String department, String accessLevel) {
        super(username, password, fullName, email, ROLE);
        this.department = department;
        this.accessLevel = accessLevel;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Polymorphic implementation of displayInfo.
     */
    @Override
    public String displayInfo() {
        return String.format(
            "=== ADMIN PROFILE ===\n" +
            "ID:          %d\n" +
            "Username:    %s\n" +
            "Full Name:   %s\n" +
            "Email:       %s\n" +
            "Department:  %s\n" +
            "Access:      %s\n" +
            "=====================",
            getUserId(), getUsername(), getFullName(),
            getEmail(), department, accessLevel
        );
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-15s | %-20s | %-25s | %-12s | %-8s |",
                getUserId(), getUsername(), getFullName(), getEmail(), department, accessLevel);
    }

    public static String getTableHeader() {
        return String.format("| %-4s | %-15s | %-20s | %-25s | %-12s | %-8s |",
                "ID", "Username", "Full Name", "Email", "Department", "Access");
    }

    public static String getTableDivider() {
        return "+------+-----------------+----------------------+---------------------------+--------------+----------+";
    }
}
