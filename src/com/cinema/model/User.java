package com.cinema.model;

/**
 * Abstract base class for system users.
 * Demonstrates abstraction and inheritance (Admin and Customer extend this).
 */
public abstract class User {

    private String username;
    private String password;
    private String role;

    protected User() {}

    protected User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    /**
     * Abstract method demonstrating polymorphism.
     */
    public abstract String displayInfo();

    public boolean validateCredentials(String inputUser, String inputPass) {
        return this.username != null && this.password != null
                && this.username.equals(inputUser) && this.password.equals(inputPass);
    }
}
