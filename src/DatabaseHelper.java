/* ============================================
 * ADDITIONAL MODULE 1: Database Helper
 * Cinema Ticket Reservation System
 * Author: Group 1 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Handles all SQLite database operations
 * ============================================ */

import java.io.*; 
import java.nio.file.*;
import java.util.*; // Includes the Scanner class
import java.util.Scanner;

public class DatabaseHelper {

    private static final String DB_PATH = "db" + File.separator + "cinema.db";

    // FUNCTION: Executes a SQL query and returns results as a list of maps
    public static List<Map<String, String>> query(String sql) {
        List<Map<String, String>> results = new ArrayList<>();
        try {
            String wrapped = sql.trim();
            if (!wrapped.endsWith(";")) wrapped += ";";

            ProcessBuilder pb = new ProcessBuilder("sqlite3", "-header", "-separator", "|||", DB_PATH);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (OutputStream os = process.getOutputStream()) {
                os.write((wrapped + "\n.quit\n").getBytes());
                os.flush();
            }

            String output = new String(process.getInputStream().readAllBytes()).trim();
            process.waitFor();

            if (output.isEmpty()) return results;

            String[] lines = output.split("\n");
            if (lines.length == 0) return results;

            String[] headers = lines[0].split("\\|\\|\\|", -1);
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) continue;
                String[] values = line.split("\\|\\|\\|", -1);
                Map<String, String> row = new LinkedHashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    row.put(headers[j].trim(), j < values.length ? values[j].trim() : "");
                }
                results.add(row);
            }
        } catch (Exception e) {
            ExceptionHandler.handle(e, "Database query");
        }
        return results;
    }

    // FUNCTION: Executes SQL statements (INSERT, UPDATE, DELETE)
    public static boolean execute(String sql) {
        try {
            String wrapped = sql.trim();
            if (!wrapped.endsWith(";")) wrapped += ";";

            ProcessBuilder pb = new ProcessBuilder("sqlite3", DB_PATH);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (OutputStream os = process.getOutputStream()) {
                os.write((wrapped + "\n.quit\n").getBytes());
                os.flush();
            }

            String output = new String(process.getInputStream().readAllBytes()).trim();
            int exitCode = process.waitFor();
            if (exitCode != 0 && output.contains("Error")) {
                System.out.println("  [!] SQL Error: " + output);
                return false;
            }
            return true;
        } catch (Exception e) {
            ExceptionHandler.handle(e, "Database execute");
            return false;
        }
    }

    // FUNCTION: Executes the SQL script file
    public static boolean executeScript(String scriptPath) {
        try {
            String sql = Files.readString(Paths.get(scriptPath));
            ProcessBuilder pb = new ProcessBuilder("sqlite3", DB_PATH);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (OutputStream os = process.getOutputStream()) {
                os.write(sql.getBytes());
                os.flush();
            }

            process.getInputStream().readAllBytes();
            return process.waitFor() == 0;
        } catch (Exception e) {
            ExceptionHandler.handle(e, "Execute script");
            return false;
        }
    }

    // FUNCTION: Initializes the database if it doesn't exist
    public static void initialize() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists() && dbFile.length() > 0) {
            // FUNCTION: Verify
            List<Map<String, String>> test = query("SELECT COUNT(*) as c FROM movie");
            if (!test.isEmpty()) return;
        }

        System.out.println("  [*] Initializing database...");
        new File("db").mkdirs();

        String schemaPath = "db" + File.separator + "schema.sql";
        String dataPath = "db" + File.separator + "sample_data.sql";

        if (Files.exists(Paths.get(schemaPath))) {
            executeScript(schemaPath);
            System.out.println("  [+] Schema created.");
        } else {
            System.out.println("  [!] schema.sql not found!");
            System.exit(1);
        }

        if (Files.exists(Paths.get(dataPath))) {
            executeScript(dataPath);
            System.out.println("  [+] Sample data loaded.");
        }
    }

    // FUNCTION: Escapes single quotes for SQL "safety"
    public static String escape(String value) {
        if (value == null) return "";
        return value.replace("'", "''");
    }
}
