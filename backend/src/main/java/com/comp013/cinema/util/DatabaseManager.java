package com.comp013.cinema.util;

import com.comp013.cinema.exception.DatabaseConnectionException;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private static final String DB_PATH = Path.of("..", "database", "cinema.db").toString();
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

    private DatabaseManager() {
    }

    public static Connection getConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Unable to connect to SQLite database.", e);
        }
    }
}
