/* ============================================
 * MODULE 2: Main Menu Module
 * Cinema Ticket Reservation System
 * Author: Group 1 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: 
 * ADMIN ACCESS: Full access to all features (View, Search, Add, Update, Delete, Reserve, Reports)
 * CUSTOMER ACCESS (restricted):
 *   VIEW    - view movies, view screenings, view their OWN customer record, view their OWN transaction records
 *   SEARCH  - search movies, search screenings, search their OWN transaction records
 *   RESERVE - reserve a seat
 *   (Customers CANNOT access: Add, Update, Delete, Reports, or other customers' data)
 * ============================================ */

public class MainMenuModule {
    // FUNCTION: Displays and handles the main menu loop
    //           Provides navigation to system modules based on role
    public static void show() {
        boolean running = true;

        while (running) {
            if (LoginModule.isAdmin()) {
                // FUNCTION: Redirect to ADMIN MENU - Full access to all features
                running = showAdminMenu();
            } else {
                // FUNCTION: Redirect to CUSTOMER MENU - Restricted access
                running = showCustomerMenu();
            }
        }
    }

    // FUNCTION: Show ALL system features for ADMIN access
    private static boolean showAdminMenu() {
        System.out.println("\n  ============================================");
        System.out.println("     ADMIN MENU - Logged in as: " + LoginModule.getName());
        System.out.println("     Role: " + LoginModule.getRole());
        System.out.println("  ============================================");
        System.out.println("  [1] View Records");
        System.out.println("  [2] Search Record");
        System.out.println("  [3] Add Record");
        System.out.println("  [4] Update Record");
        System.out.println("  [5] Delete Record");
        System.out.println("  [6] Reserve a Seat");
        System.out.println("  [7] Generate Report");
        System.out.println("  [8] Logout");
        System.out.println();

        int choice = InputValidator.getInt("Enter choice", 1, 8);

        try {
            switch (choice) {
                case 1 -> ViewRecordsModule.show();       // FUNCTION: Admin can view ALL records
                case 2 -> SearchRecordModule.show();      // FUNCTION: Admin can search ALL records
                case 3 -> AddRecordModule.show();         // FUNCTION: Admin only adding records
                case 4 -> UpdateRecordModule.show();      // FUNCTION: Admin only updating record
                case 5 -> DeleteRecordModule.show();      // FUNCTION: Admin only deleting record
                case 6 -> reserveSeat();                  // FUNCTION: Reserve seats
                case 7 -> ReportModule.show();            // FUNCTION: Admin only report generation
                case 8 -> {
                    LoginModule.logout();
                    return false;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.handle(e, "Admin Menu");
        }
        return true;
    }

    // FUNCTION: Show RESTRICTED system features for CUSTOMER access
    //           Customers CANNOT: Add, Update, Delete records, or Generate Reports.
    private static boolean showCustomerMenu() {
        System.out.println("\n  ============================================");
        System.out.println("     CUSTOMER MENU - Logged in as: " + LoginModule.getName());
        System.out.println("     Customer No: " + LoginModule.getId());
        System.out.println("  ============================================");
        System.out.println("  [1] View Movies");
        System.out.println("  [2] View Screenings");
        System.out.println("  [3] View My Profile");
        System.out.println("  [4] View My Transactions");
        System.out.println("  [5] Search Movie");
        System.out.println("  [6] Search Screening");
        System.out.println("  [7] Search My Transactions");
        System.out.println("  [8] Reserve a Seat");
        System.out.println("  [9] Logout");
        System.out.println();

        int choice = InputValidator.getInt("Enter choice", 1, 9);

        try {
            switch (choice) {
                //  FUNCTION: VIEW - only movies, screenings, OWN profile, OWN transactions
                case 1 -> ViewRecordsModule.displayMovies();
                case 2 -> ViewRecordsModule.displayScreenings();
                case 3 -> ViewRecordsModule.displayMyProfile();           // OWN record only
                case 4 -> ViewRecordsModule.displayMyTransactions();      // OWN transactions only

                // FUNCTION: SEARCH - only movies, screenings, OWN transactions
                case 5 -> SearchRecordModule.searchMovie();
                case 6 -> SearchRecordModule.searchScreening();
                case 7 -> SearchRecordModule.searchMyTransactions();      // OWN transactions only

                // FUNCTION: Reserve a seat
                case 8 -> reserveSeat();

                case 9 -> {
                    LoginModule.logout();
                    return false;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.handle(e, "Customer Menu");
        }
        return true;
    }


    // FUNCTION: Seat reservation flow
    //           Displays screenings, seat map, and processes booking
    private static void reserveSeat() {
        System.out.println("\n  -------- RESERVE A SEAT --------");

        // FUNCTION: Show available screenings
        ViewRecordsModule.displayScreenings();

        String screeningId = InputValidator.getString("Enter Screening ID (e.g. SUN-1)");

        // FUNCTION: Verify if screening exists in the database
        var screening = DatabaseHelper.query(String.format(
            "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price FROM screenings s " +
            "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
            "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
            "WHERE s.screening_id = '%s'", DatabaseHelper.escape(screeningId)));

        if (screening.isEmpty()) {
            ExceptionHandler.handleNotFound("Screening", screeningId);
            return;
        }

        var scr = screening.get(0);
        System.out.println("\n  Movie: " + scr.get("movie_title"));
        System.out.println("  Date:  " + scr.get("screening_date") + " at " + scr.get("time_slot"));
        System.out.println("  Type:  " + scr.get("seat_type") + " | Price: PHP " + scr.get("ticket_price"));

        // FUNCTION: Show seat map
        displaySeatMap(screeningId);

        // FUNCTION: Get seat selection
        String seat;
        while (true) {
            seat = InputValidator.getString("Enter seat (e.g. A5)").toUpperCase();
            if (InputValidator.isValidSeat(seat)) break;
            ExceptionHandler.handleInvalidInput("Seat", "Must be A-J followed by 1-10");
        }

        // FUNCTION: Checks if seat is already booked
        var booked = DatabaseHelper.query(String.format(
            "SELECT * FROM \"transaction\" WHERE screening_id='%s' AND seat_no='%s' AND status='CONFIRMED'",
            DatabaseHelper.escape(screeningId), DatabaseHelper.escape(seat)));

        if (!booked.isEmpty()) {
            ExceptionHandler.handleReservationError("Seat " + seat + " is already taken!");
            return;
        }

        // FUNCTION: Confirms reservation and books seat
        double price = ExceptionHandler.safeParseDouble(scr.get("ticket_price"), 0);
        System.out.printf("\n  Total: PHP %.2f for seat %s%n", price, seat);

        if (!InputValidator.confirm("Confirm reservation?")) {
            System.out.println("  [i] Cancelled.");
            return;
        }

        // FUNCTION: Inserts transaction into the database
        int custNo = LoginModule.getId();
        String txnId = scr.get("seat_type_id") + "-M" + String.format("%02d",
            ExceptionHandler.safeParseInt(scr.get("movie_id"), 0)) + "-" + custNo;

        boolean success = DatabaseHelper.execute(String.format(
            "INSERT INTO \"transaction\" (transaction_id, transaction_date, transaction_time, " +
            "customer_no, seat_no, screening_id, movie_id, seat_type_id, reservation_type, " +
            "booking_fee, ticket_price, discount_type, discount_amount, payment_method, total_payment, status) " +
            "VALUES ('%s', date('now'), time('now'), %d, '%s', '%s', %s, %s, 'Online', " +
            "20.00, %s, 'N/A', 0, 'E-Wallet', %.2f, 'CONFIRMED')",
            DatabaseHelper.escape(txnId), custNo, DatabaseHelper.escape(seat),
            DatabaseHelper.escape(screeningId), scr.get("movie_id"),
            scr.get("seat_type_id"), scr.get("ticket_price"), price + 20.00));

        if (success) {
            System.out.println("\n  [+] RESERVATION CONFIRMED!");
            System.out.println("  Transaction ID: " + txnId);
            System.out.println("  Seat: " + seat);
            System.out.printf("  Total Paid: PHP %.2f%n", price + 20.00);
        } else {
            ExceptionHandler.handleReservationError("Could not complete booking.");
        }
        InputValidator.pause();
    }

    // FUNCTION: Displays the ASCII seat map for a screening
    //           Marks reserved seats with XX and available seats with --
    private static void displaySeatMap(String screeningId) {
        var booked = DatabaseHelper.query(String.format(
            "SELECT seat_no FROM \"transaction\" WHERE screening_id='%s' AND status='CONFIRMED' AND seat_no IS NOT NULL",
            DatabaseHelper.escape(screeningId)));

        java.util.Set<String> bookedSeats = new java.util.HashSet<>();
        for (var row : booked) {
            String sn = row.get("seat_no");
            if (sn != null && !sn.isEmpty()) bookedSeats.add(sn);
        }

        System.out.println("\n            ========== SCREEN ==========\n");
        System.out.println("      1    2    3    4    5    6    7    8    9   10");
        System.out.println("    +----+----+----+----+----+----+----+----+----+----+");

        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (String r : rows) {
            System.out.print("  " + r + " |");
            for (int c = 1; c <= 10; c++) {
                String seatNo = r + c;
                System.out.print(bookedSeats.contains(seatNo) ? " XX |" : " -- |");
            }
            System.out.println("\n    +----+----+----+----+----+----+----+----+----+----+");
        }
        System.out.println("\n  Legend: [ -- ] Available   [ XX ] Reserved");
    }
}
