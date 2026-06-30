/* ============================================
 * MODULE 10: Exception Handling Module
 * Cinema Ticket Reservation System
 * Author: Group 2 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Handles exceptions in the system using try-catch blocks.
            Once error is encountered, it gives error messages for the user.
 * ============================================ */

public class ExceptionHandler {
    // FUNCTION: Handles a general exception with context message
    public static void handle(Exception e, String context) {
        System.out.println("\n  [!] ERROR in " + context + ": " + e.getMessage());
    }

    // FUNCTION: Handles database connection errors
    public static void handleDatabaseError(Exception e) {
        System.out.println("\n  [!] DATABASE ERROR: " + e.getMessage());
        System.out.println("  [i] Please ensure sqlite3 is installed and accessible.");
    }

    // FUNCTION: Handles invalid login attempts
    public static void handleLoginError(String username) {
        System.out.println("\n  [!] LOGIN FAILED: Invalid username or password.");
        System.out.println("  [i] Attempted username: " + username);
    }

    // FUNCTION: Handles record not found errors
    public static void handleNotFound(String recordType, String id) {
        System.out.println("\n  [!] " + recordType + " not found with ID: " + id);
    }

    // FUNCTION: Handles invalid input errors
    public static void handleInvalidInput(String field, String reason) {
        System.out.println("\n  [!] Invalid input for '" + field + "': " + reason);
    }

    // FUNCTION: Handles seat reservation errors
    public static void handleReservationError(String message) {
        System.out.println("\n  [!] RESERVATION ERROR: " + message);
    }

    // FUNCTION: Safe integer parsing with try-catch
    public static int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // FUNCTION: Safe double parsing with try-catch
    public static double safeParseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
