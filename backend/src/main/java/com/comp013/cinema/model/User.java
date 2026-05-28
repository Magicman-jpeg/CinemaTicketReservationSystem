package com.comp013.cinema.model;

public abstract class User {
    protected String username;
    protected String password;
    protected String name;

    protected User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();
}
