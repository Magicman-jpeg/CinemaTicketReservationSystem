/* ============================================
 * MODULE 7: Delete Record Module
 * Cinema Ticket Reservation System
 * Author: Group 2 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Allows authorized users to delete records from the database.
            Ask for confirmation before deleting a record.
 * ============================================ */

import java.util.*; // Includes the Scanner class
import java.util.Scanner;

public class DeleteRecordModule {
    // FUNCTION: Display the Menu for Deleting Records
    public static void show() {
        System.out.println("\n  -------- DELETE RECORD --------");
        System.out.println("  [1] Delete Movie");
        System.out.println("  [2] Delete Customer");
        System.out.println("  [3] Delete Screening");
        System.out.println("  [4] Cancel Transaction");
        System.out.println("  [5] Back");

        // FUNCTION: Redirects the ADMIN to the method of their chosen action
        int choice = InputValidator.getInt("Choose", 1, 5);
        switch (choice) {
            case 1 -> deleteMovie();
            case 2 -> deleteCustomer();
            case 3 -> deleteScreening();
            case 4 -> cancelTransaction();
        }
    }

    // FUNCTION: Deletes a new movie record from the database
    //           Confirms existence and asks for user confirmation before deletion
    private static void deleteMovie() {
        int id = InputValidator.getInt("Enter Movie ID to delete");

        var results = DatabaseHelper.query(String.format(
            "SELECT movie_title FROM movie WHERE movie_id = %d", id));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Movie", String.valueOf(id));
            InputValidator.pause();
            return;
        }

        System.out.println("  Movie: " + results.get(0).get("movie_title"));

        // FUNCTION: Confirmation before deletion
        if (InputValidator.confirm("Are you sure you want to DELETE this movie?")) {
            DatabaseHelper.execute(String.format("DELETE FROM movie WHERE movie_id = %d", id));
            System.out.println("  [+] Movie deleted.");
        } else {
            System.out.println("  [i] Deletion cancelled.");
        }
        InputValidator.pause();
    }

    // FUNCTION: Deletes a customer record from the database
    //           Confirms existence and asks for user confirmation before deletion
    private static void deleteCustomer() {
        int no = InputValidator.getInt("Enter Customer No to delete");

        var results = DatabaseHelper.query(String.format(
            "SELECT name FROM customer WHERE customer_no = %d", no));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Customer", String.valueOf(no));
            InputValidator.pause();
            return;
        }

        System.out.println("  Customer: " + results.get(0).get("name"));

        // FUNCTION: Confirmation before deletion
        if (InputValidator.confirm("Are you sure you want to DELETE this customer?")) {
            DatabaseHelper.execute(String.format("DELETE FROM customer WHERE customer_no = %d", no));
            System.out.println("  [+] Customer deleted.");
        } else {
            System.out.println("  [i] Deletion cancelled.");
        }
        InputValidator.pause();
    }

    // FUNCTION: Deletes a screening record from the database
    //           Confirms existence and asks for user confirmation before deletion
    private static void deleteScreening() {
        String id = InputValidator.getString("Enter Screening ID to delete");

        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM screenings WHERE screening_id = '%s'", DatabaseHelper.escape(id)));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Screening", id);
            InputValidator.pause();
            return;
        }

        // FUNCTION: Confirmation before deletion
        if (InputValidator.confirm("Are you sure you want to DELETE screening " + id + "?")) {
            DatabaseHelper.execute(String.format("DELETE FROM screenings WHERE screening_id = '%s'",
                DatabaseHelper.escape(id)));
            System.out.println("  [+] Screening deleted.");
        } else {
            System.out.println("  [i] Deletion cancelled.");
        }
        InputValidator.pause();
    }

    // FUNCTION: Cancels a transaction by transaction ID
    //           Updates status to CANCELLED and releases the seat
    private static void cancelTransaction() {
        String id = InputValidator.getString("Enter Transaction ID to cancel");

        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM \"transaction\" WHERE transaction_id = '%s'", DatabaseHelper.escape(id)));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Transaction", id);
            InputValidator.pause();
            return;
        }

        System.out.println("  Transaction: " + id);
        System.out.println("  Seat: " + results.get(0).get("seat_no"));
        System.out.println("  Amount: PHP " + results.get(0).get("total_payment"));

        // FUNCTION: Confirmation before cancelation
        if (InputValidator.confirm("Are you sure you want to CANCEL this transaction?")) {
            DatabaseHelper.execute(String.format(
                "UPDATE \"transaction\" SET status='CANCELLED' WHERE transaction_id='%s'",
                DatabaseHelper.escape(id)));
            System.out.println("  [+] Transaction cancelled. Seat released.");
        } else {
            System.out.println("  [i] Cancellation aborted.");
        }
        InputValidator.pause();
    }
}
