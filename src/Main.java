/**
 * CINEMA TICKET RESERVATION SYSTEM
 * COMP 013 - Object-Oriented Programming Final Project
 * Group 2 - Cinema Hall Ticket Sales
 *
 * Entry point. Initializes the database and starts the login loop.
 *
 * MODULES:
 *  1. LoginModule.java       - User authentication
 *  2. MainMenuModule.java    - Main menu navigation
 *  3. AddRecordModule.java   - Add new records
 *  4. ViewRecordsModule.java - Display records
 *  5. SearchRecordModule.java- Search records
 *  6. UpdateRecordModule.java- Update records
 *  7. DeleteRecordModule.java- Delete records
 *  8. ReportModule.java      - Generate reports
 *  9. InputValidator.java    - Input validation
 * 10. ExceptionHandler.java  - Exception handling
 *     + DatabaseHelper.java  - Database connectivity
 *     + Main.java            - Entry point (this file)
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Initialize database
            DatabaseHelper.initialize();

            // Login loop
            boolean running = true;
            while (running) {
                if (LoginModule.login()) {
                    MainMenuModule.show();
                } else {
                    if (!InputValidator.confirm("Try again?")) {
                        running = false;
                    }
                }
            }

            System.out.println("\n  ============================================");
            System.out.println("    Thank you for using the Cinema System!");
            System.out.println("    COMP 013 - Group 2 Final Project");
            System.out.println("  ============================================\n");

        } catch (Exception e) {
            ExceptionHandler.handleDatabaseError(e);
        }
    }
}
