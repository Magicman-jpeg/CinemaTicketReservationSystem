/* ============================================
 * MODULE 8: Report Generation Module
 * Cinema Ticket Reservation System
 * Author: Group 1 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Generates simple reports or summaries.
 * ============================================ */

import java.util.*; // Includes the Scanner class
import java.util.Scanner;

public class ReportModule {
    // FUNCTION: Displays the Menu for Reports
    public static void show() {
        System.out.println("\n  -------- REPORTS --------");
        System.out.println("  [1] Sales Summary");
        System.out.println("  [2] Daily Revenue Report");
        System.out.println("  [3] Popular Movies");
        System.out.println("  [4] Back");

        // FUNCTION: Redirects the user to the method of their chosen action
        int choice = InputValidator.getInt("Choose", 1, 4);
        switch (choice) {
            case 1 -> salesSummary();
            case 2 -> dailyRevenue();
            case 3 -> popularMovies();
        }
    }

    // FUNCTION: Generates a sales summary report
    //           Displays total revenue, transactions, movies, customers, and screenings
    private static void salesSummary() {
        System.out.println("\n  ============================================");
        System.out.println("       CINEMA SALES SUMMARY REPORT");
        System.out.println("  ============================================");

        var rev = DatabaseHelper.query(
            "SELECT COALESCE(SUM(total_payment), 0) as total FROM \"transaction\" WHERE status='CONFIRMED'");
        var txnCount = DatabaseHelper.query("SELECT COUNT(*) as c FROM \"transaction\" WHERE status='CONFIRMED'");
        var movieCount = DatabaseHelper.query("SELECT COUNT(*) as c FROM movie");
        var custCount = DatabaseHelper.query("SELECT COUNT(*) as c FROM customer");
        var scrCount = DatabaseHelper.query("SELECT COUNT(*) as c FROM screenings");

        double total = 0;
        if (!rev.isEmpty()) total = ExceptionHandler.safeParseDouble(rev.get(0).get("total"), 0);

        System.out.printf("  Total Revenue:      PHP %,.2f%n", total);
        System.out.println("  Total Transactions: " + (txnCount.isEmpty() ? "0" : txnCount.get(0).get("c")));
        System.out.println("  Total Movies:       " + (movieCount.isEmpty() ? "0" : movieCount.get(0).get("c")));
        System.out.println("  Total Customers:    " + (custCount.isEmpty() ? "0" : custCount.get(0).get("c")));
        System.out.println("  Total Screenings:   " + (scrCount.isEmpty() ? "0" : scrCount.get(0).get("c")));
        System.out.println("  ============================================");
        InputValidator.pause();
    }

    // FUNCTION: Generates a daily revenue report
    //           Groups transactions by date and shows revenue and ticket count
    private static void dailyRevenue() {
        System.out.println("\n  -------- DAILY REVENUE --------");
        var data = DatabaseHelper.query(
            "SELECT transaction_date, SUM(total_payment) as revenue, COUNT(*) as tickets " +
            "FROM \"transaction\" WHERE status='CONFIRMED' GROUP BY transaction_date ORDER BY transaction_date");

        if (data.isEmpty()) { System.out.println("  No revenue data."); InputValidator.pause(); return; }

        System.out.printf("  %-12s | %12s | %8s%n", "Date", "Revenue", "Tickets");
        System.out.println("  " + "-".repeat(40));
        for (var row : data) {
            System.out.printf("  %-12s | PHP %8s | %8s%n",
                row.get("transaction_date"), row.get("revenue"), row.get("tickets"));
        }
        System.out.println("  " + "-".repeat(40));
        InputValidator.pause();
    }

    // FUNCTION: Generates a report of popular movies by sales
    //           Displays ranking, tickets sold, and revenue per movie
    private static void popularMovies() {
        System.out.println("\n  -------- POPULAR MOVIES (by sales) --------");
        var data = DatabaseHelper.query(
            "SELECT m.movie_title, COUNT(t.transaction_id) as sold, SUM(t.total_payment) as revenue " +
            "FROM \"transaction\" t JOIN movie m ON t.movie_id = m.movie_id " +
            "WHERE t.status='CONFIRMED' GROUP BY m.movie_id ORDER BY sold DESC");

        if (data.isEmpty()) { System.out.println("  No data."); InputValidator.pause(); return; }

        System.out.printf("  %-3s | %-32s | %7s | %10s%n", "#", "Movie", "Tickets", "Revenue");
        System.out.println("  " + "-".repeat(60));
        int rank = 1;
        for (var row : data) {
            System.out.printf("  %-3d | %-32s | %7s | PHP %6s%n",
                rank++, row.get("movie_title"), row.get("sold"), row.get("revenue"));
        }
        InputValidator.pause();
    }
}
