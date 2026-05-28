package com.comp013.cinema.model;

public class Admin extends User {
    private final int adminId;

    public Admin(int adminId, String username, String password, String name) {
        super(username, password, name);
        this.adminId = adminId;
    }

    public int getAdminId() {
        return adminId;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
