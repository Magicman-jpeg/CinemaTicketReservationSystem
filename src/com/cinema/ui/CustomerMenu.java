package com.cinema.ui;

import com.cinema.dao.*;
import com.cinema.model.*;
import com.cinema.service.ReservationService;
import com.cinema.exception.*;

import java.util.List;

/**
 * Console menu for Customer users.
 * Provides movie browsing, seat reservation, and booking management.
 */
public class CustomerMenu {

    private final Customer customer;
    private final MovieDAO movieDAO;
    private final ScreeningDAO screeningDAO;
    private final ReservationService reservationService;

    public CustomerMenu(Customer customer) {
        this.customer = customer;
        this.movieDAO = new MovieDAO();
        this.screeningDAO = new ScreeningDAO();
        this.reservationService = new ReservationService();
    }

    /**
     * Main customer menu loop.
     */
    public void show() {
        boolean running = true;
        while (running) {
            String[] options = {
                "Browse Movies",
                "View Screenings",
                "Reserve a Seat",
                "My Reservations",
                "Cancel Reservation",
                "View My Profile",
                "Logout"
            };
            int choice = ConsoleUtils.showMenu(
                "CUSTOMER PANEL - Welcome, " + customer.getFullName(), options);

            try {
                switch (choice) {
                    case 1 -> browseMovies();
                    case 2 -> viewScreenings();
                    case 3 -> reserveSeat();
                    case 4 -> viewMyReservations();
                    case 5 -> cancelReservation();
                    case 6 -> System.out.println(customer.displayInfo());
                    case 7 -> running = false;
                }
            } catch (DatabaseConnectionException e) {
                ConsoleUtils.showError("Database error: " + e.getMessage());
            }
        }
        ConsoleUtils.showInfo("Logged out successfully.");
    }


    private void browseMovies() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("NOW SHOWING");
        List<Movie> movies = movieDAO.findAll();
        if (movies.isEmpty()) {
            ConsoleUtils.showInfo("No movies currently showing.");
        } else {
            System.out.println(Movie.getTableDivider());
            System.out.println(Movie.getTableHeader());
            System.out.println(Movie.getTableDivider());
            for (Movie m : movies) {
                System.out.println(m.toString());
            }
            System.out.println(Movie.getTableDivider());

            // Option to search
            if (ConsoleUtils.confirm("Search for a specific movie?")) {
                String keyword = ConsoleUtils.getInput("Enter keyword");
                List<Movie> results = movieDAO.searchByTitle(keyword);
                if (results.isEmpty()) {
                    ConsoleUtils.showInfo("No matches found.");
                } else {
                    for (Movie m : results) {
                        System.out.println(m.toString());
                    }
                }
            }
        }
        ConsoleUtils.pause();
    }

    private void viewScreenings() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("AVAILABLE SCREENINGS");
        List<Screening> screenings = screeningDAO.findAll();
        if (screenings.isEmpty()) {
            ConsoleUtils.showInfo("No screenings available.");
        } else {
            System.out.println(Screening.getTableDivider());
            System.out.println(Screening.getTableHeader());
            System.out.println(Screening.getTableDivider());
            for (Screening s : screenings) {
                System.out.println(s.toString());
            }
            System.out.println(Screening.getTableDivider());
        }
        ConsoleUtils.pause();
    }


    private void reserveSeat() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("RESERVE A SEAT");

        // Show available screenings
        List<Screening> screenings = screeningDAO.findAll();
        if (screenings.isEmpty()) {
            ConsoleUtils.showInfo("No screenings available.");
            ConsoleUtils.pause();
            return;
        }
        System.out.println(Screening.getTableDivider());
        System.out.println(Screening.getTableHeader());
        System.out.println(Screening.getTableDivider());
        for (Screening s : screenings) {
            System.out.println(s.toString());
        }
        System.out.println(Screening.getTableDivider());

        int screeningId = ConsoleUtils.getIntInput("Select Screening ID");
        Screening selected = screeningDAO.findById(screeningId);
        if (selected == null) {
            ConsoleUtils.showError("Invalid screening ID.");
            ConsoleUtils.pause();
            return;
        }

        // Display the movie info
        Movie movie = movieDAO.findById(selected.getMovieId());
        if (movie != null) {
            System.out.println("\n  Movie: " + movie.getTitle());
            System.out.println("  Date:  " + selected.getScreenDate() + " at " + selected.getScreenTime());
            System.out.println("  Hall:  " + selected.getHallNumber());
            System.out.printf("  Price: PHP %.2f%n", selected.getTicketPrice());
            if (customer.getDiscount() > 0) {
                System.out.printf("  Your Discount: %.0f%% (%s member)%n",
                        customer.getDiscount() * 100, customer.getMembershipType());
            }
        }

        // Show seat map
        String seatMap = reservationService.generateSeatMap(screeningId);
        System.out.println(seatMap);

        // Get seat selection
        String row = ConsoleUtils.getInput("Enter Row (A-K)").toUpperCase();
        int col = ConsoleUtils.getIntInput("Enter Column", 1, 10);

        // Select payment method
        String[] payMethods = {"CASH", "CREDIT_CARD", "DEBIT_CARD", "GCASH"};
        System.out.println("\n  Payment Methods:");
        for (int i = 0; i < payMethods.length; i++) {
            System.out.printf("    [%d] %s%n", i + 1, payMethods[i]);
        }
        int payChoice = ConsoleUtils.getIntInput("Select payment method", 1, 4);
        String paymentMethod = payMethods[payChoice - 1];

        // Confirm booking
        double discount = customer.getDiscount();
        double finalPrice = selected.getTicketPrice() * (1.0 - discount);
        System.out.printf("\n  Seat: %s%d | Payment: %s | Total: PHP %.2f%n",
                row, col, paymentMethod, finalPrice);

        if (!ConsoleUtils.confirm("Confirm reservation?")) {
            ConsoleUtils.showInfo("Reservation cancelled.");
            ConsoleUtils.pause();
            return;
        }

        // Process reservation
        try {
            Transaction txn = reservationService.reserveSeat(
                    customer.getUserId(), screeningId, row, col, paymentMethod, discount);
            ConsoleUtils.showSuccess("Reservation confirmed!");
            System.out.println("\n  ┌─────────────────────────────────────┐");
            System.out.println("  │        BOOKING CONFIRMATION         │");
            System.out.println("  ├─────────────────────────────────────┤");
            System.out.printf( "  │  Transaction ID: %-18d │%n", txn.getTransactionId());
            System.out.printf( "  │  Movie:          %-18s │%n",
                    movie != null ? movie.getTitle().substring(0, Math.min(18, movie.getTitle().length())) : "N/A");
            System.out.printf( "  │  Seat:           %-18s │%n", txn.getSeatLabel());
            System.out.printf( "  │  Amount:         PHP %-14.2f │%n", txn.getAmountPaid());
            System.out.printf( "  │  Status:         %-18s │%n", txn.getStatus());
            System.out.println("  └─────────────────────────────────────┘");
        } catch (InvalidSeatException e) {
            ConsoleUtils.showError(e.getMessage());
        } catch (DuplicateReservationException e) {
            ConsoleUtils.showError(e.getMessage());
        } catch (PaymentFailedException e) {
            ConsoleUtils.showError(e.getMessage());
        }
        ConsoleUtils.pause();
    }


    private void viewMyReservations() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("MY RESERVATIONS");
        List<Transaction> reservations = reservationService.getCustomerReservations(customer.getUserId());
        if (reservations.isEmpty()) {
            ConsoleUtils.showInfo("You have no reservations.");
        } else {
            System.out.println(Transaction.getTableDivider());
            System.out.println(Transaction.getTableHeader());
            System.out.println(Transaction.getTableDivider());
            for (Transaction t : reservations) {
                System.out.println(t.toString());
            }
            System.out.println(Transaction.getTableDivider());
            System.out.println("  Total bookings: " + reservations.size());
        }
        ConsoleUtils.pause();
    }

    private void cancelReservation() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("CANCEL RESERVATION");

        List<Transaction> reservations = reservationService.getCustomerReservations(customer.getUserId());
        if (reservations.isEmpty()) {
            ConsoleUtils.showInfo("You have no reservations to cancel.");
            ConsoleUtils.pause();
            return;
        }

        // Show only confirmed reservations
        System.out.println(Transaction.getTableDivider());
        System.out.println(Transaction.getTableHeader());
        System.out.println(Transaction.getTableDivider());
        for (Transaction t : reservations) {
            if (Transaction.STATUS_CONFIRMED.equals(t.getStatus())) {
                System.out.println(t.toString());
            }
        }
        System.out.println(Transaction.getTableDivider());

        int txnId = ConsoleUtils.getIntInput("Enter Transaction ID to cancel");
        if (ConsoleUtils.confirm("Are you sure you want to cancel this reservation?")) {
            boolean success = reservationService.cancelReservation(txnId, customer.getUserId());
            if (success) {
                ConsoleUtils.showSuccess("Reservation cancelled. Seat released.");
            } else {
                ConsoleUtils.showError("Could not cancel. Transaction not found or already cancelled.");
            }
        } else {
            ConsoleUtils.showInfo("Cancellation aborted.");
        }
        ConsoleUtils.pause();
    }
}
