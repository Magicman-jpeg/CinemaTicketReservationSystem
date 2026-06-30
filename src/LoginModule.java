/* ============================================
 * MODULE 1: Login Module
 * Cinema Ticket Reservation System
 * Author: Group 2 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: User authentication.
            Includes:
             - Username and password input
             - Login validation using database records
             - Error message for invalid username or password
             - At least one default admin account (included in DB)
 * ============================================ */

import java.util.*; // Includes the Scanner class
import java.util.Scanner;

public class LoginModule {
    // FUNCTION: Logged-in user credentials
    private static String loggedInUsername = "";
    private static String loggedInName = "";
    private static String loggedInRole = "";
    private static int loggedInId = 0;

    // FUNCTION: Displays login screen and authenticates user.
    //           Returns TRUE if login is successful.
    public static boolean login() {
        System.out.println("\n  ============================================");
        System.out.println("       CINEMA TICKET RESERVATION SYSTEM");
        System.out.println("       COMP 009 - Group 1 Final Project");
        System.out.println("  ============================================\n");
        System.out.println("  -------- LOGIN --------");

        String username = InputValidator.getString("Username");
        String password = InputValidator.getString("Password");
        
        // FUNCTION: TRY Admin login
        try {
            List<Map<String, String>> result = DatabaseHelper.query(
                String.format("SELECT * FROM admin WHERE admin_username='%s' AND admin_pass='%s'",
                    DatabaseHelper.escape(username), DatabaseHelper.escape(password)));

            if (!result.isEmpty()) {
                loggedInUsername = username;
                loggedInName = result.get(0).get("admin_name");
                loggedInRole = "ADMIN";
                loggedInId = ExceptionHandler.safeParseInt(result.get(0).get("admin_id"), 0);
                System.out.println("\n  [+] Welcome, " + loggedInName + "! (Admin)");
                return true;
            }

            // FUNCTION: TRY Customer login
            result = DatabaseHelper.query(
                String.format("SELECT * FROM customer WHERE customer_username='%s' AND customer_pass='%s'",
                    DatabaseHelper.escape(username), DatabaseHelper.escape(password)));

            if (!result.isEmpty()) {
                loggedInUsername = username;
                loggedInName = result.get(0).get("name");
                loggedInRole = "CUSTOMER";
                loggedInId = ExceptionHandler.safeParseInt(result.get(0).get("customer_no"), 0);
                System.out.println("\n  [+] Welcome, " + loggedInName + "! (Customer)");
                return true;
            }

            // FUNCTION: Login failed
            ExceptionHandler.handleLoginError(username);
            return false;

        } catch (Exception e) {
            ExceptionHandler.handle(e, "Login");
            return false;
        }
    }

    // FUNCTION: Getters for logged-in user info
    public static String getUsername() { return loggedInUsername; }
    public static String getName() { return loggedInName; }
    public static String getRole() { return loggedInRole; }
    public static int getId() { return loggedInId; }
    public static boolean isAdmin() { return "ADMIN".equals(loggedInRole); }

    // FUNCTION: Logs out the current user
    public static void logout() {
        System.out.println("\n  [i] Goodbye, " + loggedInName + "!");
        loggedInUsername = "";
        loggedInName = "";
        loggedInRole = "";
        loggedInId = 0;
    }
}
