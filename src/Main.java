/* ============================================
 * ADDITIONAL MODULE 2: Main
 * Cinema Ticket Reservation System
 * Author: Group 1 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Entry point.
            Initializes the database and starts the login loop.
 * ============================================ */

/* ALL MODULES IN THIS SYSTEM & THEIR PURPOSE
 *  1. LoginModule.java       - User authentication
 *  2. MainMenuModule.java    - Main menu navigation
 *  3. AddRecordModule.java   - Add new records
 *  4. ViewRecordsModule.java - Display records
 *  5. SearchRecordModule.java- Search records
 *  6. UpdateRecordModule.java- Update records
 *  7. DeleteRecordModule.java- Delete records
 *  8. ReportModule.java      - Generate reports
 *  9. InputValidator.java    - Input validation
 *  10.ExceptionHandler.java  - Exception handling
 *   + DatabaseHelper.java  - Database connectivity
 *   + Main.java            - Entry point (CURRENT FILE) */


public class Main {

    public static void main(String[] args) {
        try {
            // FUNCTION: Initializes database
            DatabaseHelper.initialize();

            // FUNCTION: Login loop
            boolean running = true;
            while (running) {
                if (LoginModule.login()) {
                    // FUNCTION: Show main menu after successful login
                    MainMenuModule.show();
                } else {
                    // FUNCTION: Ask user if they want to retry login
                    if (!InputValidator.confirm("Try again?")) {
                        running = false;
                    }
                }
            }

            // FUNCTION: Exit message
            System.out.println("\n  ============================================");
            System.out.println("    Thank you for using the Cinema System!");
            System.out.println("    COMP 009 - Group 1 Final Project");
            System.out.println("  ============================================\n");

        } catch (Exception e) {
            // FUNCTION: Handles database initialization errors
            ExceptionHandler.handleDatabaseError(e);
        }
    }
}
