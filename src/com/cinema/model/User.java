package com.cinema.model;

/**
 * Abstract base class for system users.
 * Demonstrates abstraction and inheritance (Admin and Customer extend this).
 */
public abstract class User {

    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role; // ADMIN or CUSTOMER

    // Default constructor
    protected User() {
    }

    // Parameterized constructor
    protected User(int userId, String username, String password,
                   String fullName, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Constructor without ID
    protected User(String username, String password, String fullName,
                   String email, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Abstract method demonstrating polymorphism.
     * Each subclass provides its own display behavior.
     */
    public abstract String displayInfo();

    /**
     * Validates login credentials.
     */
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, username=%s, role=%s, name=%s]",
                userId, username, role, fullName);
    }
}
