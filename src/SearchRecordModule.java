/* ============================================
 * MODULE 5: Search Record Module
 * Cinema Ticket Reservation System
 * Author: Group 1 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Allows users to search records using a unique identifier or keyword.
            Displays a message if the record is not found.
 * ============================================ */

import java.util.*; // Includes the Scanner class
import java.util.Scanner;

public class SearchRecordModule {
    // FUNCTION: Displays the Menu for Searching records
    public static void show() {
        System.out.println("\n  -------- SEARCH RECORD --------");
        System.out.println("  [1] Search Movie (by ID or title)");
        System.out.println("  [2] Search Customer (by No or name)");
        System.out.println("  [3] Search Transaction (by ID)");
        System.out.println("  [4] Search Screening (by ID)");
        System.out.println("  [5] Back");

        int choice = InputValidator.getInt("Choose", 1, 5);
        // FUNCTION: Redirects the user to the method of their chosen action
        switch (choice) {
            case 1 -> searchMovie();
            case 2 -> searchCustomer();
            case 3 -> searchTransaction();
            case 4 -> searchScreening();
        }
    }

    // FUNCTION: Searches for a movie by ID or title keyword
    //           Displays movie details if found
    public static void searchMovie() {
        String keyword = InputValidator.getString("Enter Movie ID or title keyword");
        List<Map<String, String>> results;

        try {
            int id = Integer.parseInt(keyword);
            results = DatabaseHelper.query(String.format(
                "SELECT m.*, g.movie_genre, s.movie_status FROM movie m " +
                "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
                "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
                "WHERE m.movie_id = %d", id));
        } catch (NumberFormatException e) {
            results = DatabaseHelper.query(String.format(
                "SELECT m.*, g.movie_genre, s.movie_status FROM movie m " +
                "LEFT JOIN movie_genre g ON m.genre_id = g.genre_id " +
                "LEFT JOIN movie_status s ON m.status_id = s.status_id " +
                "WHERE m.movie_title LIKE '%%%s%%'",
                DatabaseHelper.escape(keyword)));
        }


        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Movie", keyword);
        } else {
            System.out.println("\n  --- Search Results ---");
            for (var m : results) {
                System.out.println("  ID:       " + m.get("movie_id"));
                System.out.println("  Title:    " + m.get("movie_title"));
                System.out.println("  Genre:    " + m.get("movie_genre"));
                System.out.println("  Duration: " + m.get("movie_duration"));
                System.out.println("  Released: " + m.get("release_date"));
                System.out.println("  Status:   " + m.get("movie_status"));
                System.out.println("  --------------------");
            }
            System.out.println("  Found: " + results.size() + " result(s)");
        }
        InputValidator.pause();
    }

    // FUNCTION: Searches for a customer by customer number or name
    //           Displays customer details if found
    private static void searchCustomer() {
        String keyword = InputValidator.getString("Enter Customer No or name");
        List<Map<String, String>> results;

        try {
            int no = Integer.parseInt(keyword);
            results = DatabaseHelper.query(String.format(
                "SELECT * FROM customer WHERE customer_no = %d", no));
        } catch (NumberFormatException e) {
            results = DatabaseHelper.query(String.format(
                "SELECT * FROM customer WHERE name LIKE '%%%s%%'",
                DatabaseHelper.escape(keyword)));
        }

        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Customer", keyword);
        } else {
            for (var c : results) {
                System.out.println("\n  Customer No: " + c.get("customer_no"));
                System.out.println("  Name:        " + c.get("name"));
                System.out.println("  Age:         " + c.get("age"));
                System.out.println("  Email:       " + c.get("email_address"));
                System.out.println("  Mobile:      " + c.get("mobile_no"));
                System.out.println("  App User:    " + c.get("app_user"));
                System.out.println("  --------------------");
            }
        }
        InputValidator.pause();
    }


    // FUNCTION: Searches for a transaction by transaction ID
    //           Displays transaction details if found
    private static void searchTransaction() {
        String id = InputValidator.getString("Enter Transaction ID");
        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM \"transaction\" WHERE transaction_id = '%s'",
            DatabaseHelper.escape(id)));

        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Transaction", id);
        } else {
            var t = results.get(0);
            System.out.println("\n  Transaction: " + t.get("transaction_id"));
            System.out.println("  Date/Time:   " + t.get("transaction_date") + " " + t.get("transaction_time"));
            System.out.println("  Customer:    " + t.get("customer_no"));
            System.out.println("  Screening:   " + t.get("screening_id"));
            System.out.println("  Seat:        " + t.get("seat_no"));
            System.out.println("  Total:       PHP " + t.get("total_payment"));
            System.out.println("  Status:      " + t.get("status"));
        }
        InputValidator.pause();
    }

    // FUNCTION: Searches for a screening by screening ID
    //           Displays screening details if found
    public static void searchScreening() {
        String id = InputValidator.getString("Enter Screening ID");
        var results = DatabaseHelper.query(String.format(
            "SELECT s.*, m.movie_title, st.seat_type, st.ticket_price FROM screenings s " +
            "LEFT JOIN movie m ON s.movie_id = m.movie_id " +
            "LEFT JOIN seat_type st ON s.seat_type_id = st.seat_type_id " +
            "WHERE s.screening_id = '%s'", DatabaseHelper.escape(id)));

        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Screening", id);
        } else {
            var s = results.get(0);
            System.out.println("\n  Screening:   " + s.get("screening_id"));
            System.out.println("  Movie:       " + s.get("movie_title"));
            System.out.println("  Date:        " + s.get("screening_date") + " (" + s.get("screening_day") + ")");
            System.out.println("  Time:        " + s.get("time_slot"));
            System.out.println("  Cinema:      " + s.get("cinema_no"));
            System.out.println("  Seat Type:   " + s.get("seat_type"));
            System.out.println("  Price:       PHP " + s.get("ticket_price"));
        }
        InputValidator.pause();
    }

    // FUNCTION: Search the CURRENT CUSTOMER's own transactions only
    //           Customers cannot search other customers' transactions
    public static void searchMyTransactions() {
        int myId = LoginModule.getId();
        String keyword = InputValidator.getString("Enter Transaction ID to search");

        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM \"transaction\" WHERE transaction_id = '%s' AND customer_no = %d",
            DatabaseHelper.escape(keyword), myId));

        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Transaction (yours)", keyword);
        } else {
            var t = results.get(0);
            System.out.println("\n  Transaction: " + t.get("transaction_id"));
            System.out.println("  Date/Time:   " + t.get("transaction_date") + " " + t.get("transaction_time"));
            System.out.println("  Screening:   " + t.get("screening_id"));
            System.out.println("  Seat:        " + t.get("seat_no"));
            System.out.println("  Total:       PHP " + t.get("total_payment"));
            System.out.println("  Status:      " + t.get("status"));
        }
        InputValidator.pause();
    }
}
