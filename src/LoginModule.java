import java.util.*;

/**
 * MODULE 1: LoginModule
 * Handles user authentication using database records.
 * Supports admin login with username and password validation.
 * Default admin account: fairytopia / popcornmanager
 */
public class LoginModule {

    private static String loggedInUsername = "";
    private static String loggedInName = "";
    private static String loggedInRole = "";
    private static int loggedInId = 0;

    /**
     * Displays login screen and authenticates user.
     * Returns true if login is successful.
     */
    public static boolean login() {
        System.out.println("\n  ============================================");
        System.out.println("       CINEMA TICKET RESERVATION SYSTEM");
        System.out.println("       COMP 009 - Group 1 Final Project");
        System.out.println("  ============================================\n");
        System.out.println("  -------- LOGIN --------");

        String username = InputValidator.getString("Username");
        String password = InputValidator.getString("Password");

        // Try admin login first
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

            // Try customer login
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

            // Login failed
            ExceptionHandler.handleLoginError(username);
            return false;

        } catch (Exception e) {
            ExceptionHandler.handle(e, "Login");
            return false;
        }
    }

    // Getters for logged-in user info
    public static String getUsername() { return loggedInUsername; }
    public static String getName() { return loggedInName; }
    public static String getRole() { return loggedInRole; }
    public static int getId() { return loggedInId; }
    public static boolean isAdmin() { return "ADMIN".equals(loggedInRole); }

    /**
     * Logs out the current user.
     */
    public static void logout() {
        System.out.println("\n  [i] Goodbye, " + loggedInName + "!");
        loggedInUsername = "";
        loggedInName = "";
        loggedInRole = "";
        loggedInId = 0;
    }
}
