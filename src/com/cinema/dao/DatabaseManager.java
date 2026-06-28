package com.cinema.dao;

import com.cinema.exception.DatabaseConnectionException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * DatabaseManager provides SQLite database connectivity.
 * Uses the native sqlite3 CLI tool via ProcessBuilder for portability.
 * Follows the Singleton pattern to manage a single database connection.
 *
 * This approach ensures the project runs without external JAR dependencies
 * while demonstrating proper database integration patterns.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private String dbPath;
    private boolean initialized;

    // Private constructor (Singleton pattern)
    private DatabaseManager() {
        // Default database path relative to project root
        this.dbPath = getProjectDbPath();
        this.initialized = false;
    }

    /**
     * Returns the singleton instance of DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Determines the database file path.
     */
    private String getProjectDbPath() {
        String userDir = System.getProperty("user.dir");
        return userDir + File.separator + "db" + File.separator + "cinema.db";
    }

    /**
     * Returns the current database path.
     */
    public String getDbPath() {
        return dbPath;
    }

    /**
     * Sets a custom database path.
     */
    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
        this.initialized = false;
    }

    /**
     * Initializes the database by creating tables if they don't exist.
     */
    public void initializeDatabase() throws DatabaseConnectionException {
        try {
            // Ensure db directory exists
            Path dbDir = Paths.get(dbPath).getParent();
            if (dbDir != null && !Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
            }

            // Read and execute schema SQL
            String schemaPath = System.getProperty("user.dir") + File.separator + "db" + File.separator + "schema.sql";
            if (Files.exists(Paths.get(schemaPath))) {
                executeScript(schemaPath);
            }

            this.initialized = true;
        } catch (Exception e) {
            throw new DatabaseConnectionException("Failed to initialize database: " + e.getMessage(), 
                    "INIT", e);
        }
    }

    /**
     * Checks if the database is initialized.
     */
    public boolean isInitialized() {
        return initialized && Files.exists(Paths.get(dbPath));
    }

    /**
     * Executes a SQL script file against the database.
     */
    public void executeScript(String scriptPath) throws DatabaseConnectionException {
        try {
            ProcessBuilder pb = new ProcessBuilder("sqlite3", dbPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String sql = Files.readString(Paths.get(scriptPath));

            try (OutputStream os = process.getOutputStream()) {
                os.write(sql.getBytes());
                os.flush();
            }

            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new DatabaseConnectionException(
                        "Script execution failed: " + output, "EXECUTE_SCRIPT");
            }
        } catch (DatabaseConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseConnectionException(
                    "Failed to execute script: " + e.getMessage(), "EXECUTE_SCRIPT", e);
        }
    }

    /**
     * Executes a single SQL statement (INSERT, UPDATE, DELETE).
     * Returns the number of rows affected.
     */
    public int executeUpdate(String sql) throws DatabaseConnectionException {
        try {
            String wrappedSql = sql.trim();
            if (!wrappedSql.endsWith(";")) {
                wrappedSql += ";";
            }

            ProcessBuilder pb = new ProcessBuilder("sqlite3", dbPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Enable changes() function and execute
            String commands = ".changes on\n" + wrappedSql + "\n.quit\n";
            try (OutputStream os = process.getOutputStream()) {
                os.write(commands.getBytes());
                os.flush();
            }

            String output = new String(process.getInputStream().readAllBytes()).trim();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new DatabaseConnectionException(
                        "Update failed: " + output, "EXECUTE_UPDATE");
            }

            // Parse changes count from output
            // Output format: "changes:   N   total_changes:   M"
            if (output.contains("changes:")) {
                String[] parts = output.split("\\s+");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("changes:") && i + 1 < parts.length) {
                        try {
                            return Integer.parseInt(parts[i + 1]);
                        } catch (NumberFormatException e) {
                            return 1;
                        }
                    }
                }
            }
            return output.isEmpty() ? 1 : 0;
        } catch (DatabaseConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseConnectionException(
                    "Execute update failed: " + e.getMessage(), "EXECUTE_UPDATE", e);
        }
    }

    /**
     * Executes a SQL query and returns results as a list of maps.
     * Each map represents a row with column names as keys.
     */
    public List<Map<String, String>> executeQuery(String sql) throws DatabaseConnectionException {
        List<Map<String, String>> results = new ArrayList<>();

        try {
            String wrappedSql = sql.trim();
            if (!wrappedSql.endsWith(";")) {
                wrappedSql += ";";
            }

            ProcessBuilder pb = new ProcessBuilder("sqlite3", "-header", "-separator", "|||", dbPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String commands = wrappedSql + "\n.quit\n";
            try (OutputStream os = process.getOutputStream()) {
                os.write(commands.getBytes());
                os.flush();
            }

            String output = new String(process.getInputStream().readAllBytes()).trim();
            int exitCode = process.waitFor();

            if (exitCode != 0 && !output.isEmpty()) {
                throw new DatabaseConnectionException(
                        "Query failed: " + output, "EXECUTE_QUERY");
            }

            if (output.isEmpty()) {
                return results;
            }

            // Parse CSV-like output
            String[] lines = output.split("\n");
            if (lines.length == 0) {
                return results;
            }

            // First line is headers
            String[] headers = lines[0].split("\\|\\|\\|", -1);

            // Remaining lines are data
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) continue;

                String[] values = line.split("\\|\\|\\|", -1);
                Map<String, String> row = new LinkedHashMap<>();

                for (int j = 0; j < headers.length; j++) {
                    String value = (j < values.length) ? values[j] : "";
                    row.put(headers[j].trim(), value.trim());
                }
                results.add(row);
            }

            return results;
        } catch (DatabaseConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseConnectionException(
                    "Execute query failed: " + e.getMessage(), "EXECUTE_QUERY", e);
        }
    }

    /**
     * Executes an INSERT and returns the last inserted row ID.
     */
    public int executeInsert(String sql) throws DatabaseConnectionException {
        try {
            String wrappedSql = sql.trim();
            if (!wrappedSql.endsWith(";")) {
                wrappedSql += ";";
            }

            ProcessBuilder pb = new ProcessBuilder("sqlite3", dbPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String commands = wrappedSql + "\nSELECT last_insert_rowid();\n.quit\n";
            try (OutputStream os = process.getOutputStream()) {
                os.write(commands.getBytes());
                os.flush();
            }

            String output = new String(process.getInputStream().readAllBytes()).trim();
            int exitCode = process.waitFor();

            if (exitCode != 0 && output.contains("Error")) {
                throw new DatabaseConnectionException(
                        "Insert failed: " + output, "EXECUTE_INSERT");
            }

            // Last line should be the row ID
            String[] lines = output.split("\n");
            for (int i = lines.length - 1; i >= 0; i--) {
                String line = lines[i].trim();
                if (!line.isEmpty()) {
                    try {
                        return Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        // Continue searching
                    }
                }
            }
            return -1;
        } catch (DatabaseConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseConnectionException(
                    "Execute insert failed: " + e.getMessage(), "EXECUTE_INSERT", e);
        }
    }

    /**
     * Escapes a string value for safe SQL insertion.
     * Prevents SQL injection by escaping single quotes.
     */
    public static String escapeString(String value) {
        if (value == null) return "NULL";
        return value.replace("'", "''");
    }

    /**
     * Tests the database connection.
     */
    public boolean testConnection() {
        try {
            List<Map<String, String>> result = executeQuery("SELECT 1 as test;");
            return !result.isEmpty() && "1".equals(result.get(0).get("test"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a table exists in the database.
     */
    public boolean tableExists(String tableName) throws DatabaseConnectionException {
        String sql = String.format(
            "SELECT name FROM sqlite_master WHERE type='table' AND name='%s';",
            escapeString(tableName));
        List<Map<String, String>> result = executeQuery(sql);
        return !result.isEmpty();
    }

    /**
     * Gets the count of rows in a table.
     */
    public int getRowCount(String tableName) throws DatabaseConnectionException {
        String sql = String.format("SELECT COUNT(*) as cnt FROM %s;", tableName);
        List<Map<String, String>> result = executeQuery(sql);
        if (!result.isEmpty()) {
            try {
                return Integer.parseInt(result.get(0).get("cnt"));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
