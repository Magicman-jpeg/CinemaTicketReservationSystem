import java.util.*;

/**
 * MODULE 4: ViewRecordsModule
 * Displays records stored in the database.
 * Uses table-like text display for console output.
 */
public class ViewRecordsModule {

    public static void show() {
        System.out.println("\n  -------- VIEW RECORDS --------");
        System.out.println("  [1] View Movies");
        System.out.println("  [2] View Screenings");
        System.out.println("  [3] View Customers");
        System.out.println("  [4] View Transactions");
        System.out.println("  [5] Back");

        int choice = InputValidator.getInt("Choose", 1, 5);
        switch (choice) {
            case 1 -> displayMovies();
            case 2 -> displayScreenings();
            case 3 -> displayCustomers();
            case 4 -> displayTransactions();
        }
    }

    public static void displayMovies() {
        System.out.println("\n  -------- ALL MOVIES --------");
        List<Map<String, String>> movies = DatabaseHelper.query(
            "SELECT m.*, g.movie_genre, s.movie_status, a.movie_age_rating " +
            "FROM movie m " +
            "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
            "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
            "LEFT JOIN age_rating a ON m.age_rate_id = a.age_rate_id ORDER BY m.movie_id");

        if (movies.isEmpty()) { System.out.println("  No movies found."); return; }

        System.out.printf("  %-3s | %-32s | %-10s | %-14s | %-10s | %-15s | %-4s%n",
            "ID", "Title", "Genre", "Duration", "Released", "Status", "Rate");
        System.out.println("  " + "-".repeat(100));
        for (var m : movies) {
            System.out.printf("  %-3s | %-32s | %-10s | %-14s | %-10s | %-15s | %-4s%n",
                m.get("movie_id"), m.get("movie_title"), m.get("movie_genre"),
                m.get("movie_duration"), m.get("release_date"),
                m.get("movie_status"), m.get("movie_age_rating"));
        }
        System.out.println("  Total: " + movies.size() + " movies");
        InputValidator.pause();
    }


    public static void displayScreenings() {
        System.out.println("\n  -------- ALL SCREENINGS --------");
        List<Map<String, String>> list = DatabaseHelper.query(
            "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price FROM screenings s " +
            "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
            "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
            "ORDER BY s.screening_date, s.time_slot");

        if (list.isEmpty()) { System.out.println("  No screenings."); return; }

        System.out.printf("  %-7s | %-3s | %-10s | %-5s | %-5s | %-8s | %-8s | %-30s%n",
            "ID", "Day", "Date", "Time", "Hall", "Type", "Price", "Movie");
        System.out.println("  " + "-".repeat(95));
        for (var s : list) {
            System.out.printf("  %-7s | %-3s | %-10s | %-5s | %-5s | %-8s | PHP %-4s | %-30s%n",
                s.get("screening_id"), s.get("screening_day"), s.get("screening_date"),
                s.get("time_slot"), s.get("cinema_no"), s.get("seat_type"),
                s.get("ticket_price"), s.get("movie_title"));
        }
        System.out.println("  Total: " + list.size() + " screenings");
        InputValidator.pause();
    }

    public static void displayCustomers() {
        System.out.println("\n  -------- ALL CUSTOMERS --------");
        List<Map<String, String>> list = DatabaseHelper.query(
            "SELECT * FROM customer ORDER BY customer_no LIMIT 50");

        if (list.isEmpty()) { System.out.println("  No customers."); return; }

        System.out.printf("  %-6s | %-25s | %-3s | %-30s | %-13s%n",
            "No", "Name", "Age", "Email", "Mobile");
        System.out.println("  " + "-".repeat(85));
        for (var c : list) {
            System.out.printf("  %-6s | %-25s | %-3s | %-30s | %-13s%n",
                c.get("customer_no"), c.get("name"), c.get("age"),
                c.get("email_address"), c.get("mobile_no"));
        }
        System.out.println("  Total shown: " + list.size());
        InputValidator.pause();
    }

    public static void displayTransactions() {
        System.out.println("\n  -------- ALL TRANSACTIONS --------");
        List<Map<String, String>> list = DatabaseHelper.query(
            "SELECT * FROM \"transaction\" ORDER BY transaction_date DESC, transaction_time DESC");

        if (list.isEmpty()) { System.out.println("  No transactions."); return; }

        System.out.printf("  %-14s | %-10s | %-5s | %-6s | %-4s | %-7s | %-8s | %-9s%n",
            "TxnID", "Date", "Time", "CustNo", "Seat", "ScrID", "Total", "Status");
        System.out.println("  " + "-".repeat(80));
        for (var t : list) {
            System.out.printf("  %-14s | %-10s | %-5s | %-6s | %-4s | %-7s | PHP %-4s | %-9s%n",
                t.get("transaction_id"), t.get("transaction_date"), t.get("transaction_time"),
                t.get("customer_no"), t.get("seat_no") != null ? t.get("seat_no") : "N/A",
                t.get("screening_id"), t.get("total_payment"), t.get("status"));
        }
        System.out.println("  Total: " + list.size());
        InputValidator.pause();
    }
}
