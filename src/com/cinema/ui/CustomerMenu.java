package com.cinema.ui;

import com.cinema.dao.*;
import com.cinema.model.*;
import com.cinema.service.ReservationService;
import com.cinema.exception.*;
import java.util.List;

/**
 * Console menu for Customer users.
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

    public void show() {
        boolean running = true;
        while (running) {
            String[] options = { "Browse Movies", "View Screenings",
                "Reserve a Seat", "My Reservations",
                "Cancel Reservation", "View My Profile", "Logout" };
            int choice = ConsoleUtils.showMenu(
                "CUSTOMER PANEL - Welcome, " + customer.getName(), options);
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
        ConsoleUtils.showInfo("Logged out.");
    }


    private void browseMovies() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("NOW SHOWING");
        List<Movie> movies = movieDAO.findByStatus("Showing");
        System.out.println(Movie.getTableDivider());
        System.out.println(Movie.getTableHeader());
        System.out.println(Movie.getTableDivider());
        for (Movie m : movies) System.out.println(m.toString());
        System.out.println(Movie.getTableDivider());
        ConsoleUtils.pause();
    }

    private void viewScreenings() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("AVAILABLE SCREENINGS");
        List<Screening> list = screeningDAO.findAll();
        System.out.println(Screening.getTableDivider());
        System.out.println(Screening.getTableHeader());
        System.out.println(Screening.getTableDivider());
        for (Screening s : list) System.out.println(s.toString());
        System.out.println(Screening.getTableDivider());
        ConsoleUtils.pause();
    }

    private void reserveSeat() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("RESERVE A SEAT");
        // Show screenings
        List<Screening> screenings = screeningDAO.findAll();
        if (screenings.isEmpty()) { ConsoleUtils.showInfo("No screenings."); ConsoleUtils.pause(); return; }
        System.out.println(Screening.getTableDivider());
        System.out.println(Screening.getTableHeader());
        System.out.println(Screening.getTableDivider());
        for (Screening s : screenings) System.out.println(s.toString());
        System.out.println(Screening.getTableDivider());

        String screeningId = ConsoleUtils.getInput("Select Screening ID (e.g. SUN-1)");
        Screening selected = screeningDAO.findById(screeningId);
        if (selected == null) { ConsoleUtils.showError("Invalid screening."); ConsoleUtils.pause(); return; }

        System.out.printf("\n  Movie: %s\n", selected.getMovieTitle());
        System.out.printf("  Date:  %s %s at %s\n", selected.getScreeningDay(), selected.getScreeningDate(), selected.getTimeSlot());
        System.out.printf("  Seat Type: %s | Price: PHP %.2f\n", selected.getSeatTypeName(), selected.getTicketPrice());

        // Show seat map
        String seatMap = reservationService.generateSeatMap(screeningId);
        System.out.println(seatMap);

        // Get seat
        String row = ConsoleUtils.getInput("Enter Row (A-J)").toUpperCase();
        int col = ConsoleUtils.getIntInput("Enter Column", 1, 10);
        String seatNo = row + col;

        // Discount
        String discountType = "N/A";
        if (customer.getAge() >= 60) {
            discountType = "Senior Citizen";
            ConsoleUtils.showInfo("Senior Citizen discount (20%) applied!");
        } else if (ConsoleUtils.confirm("Do you have a PWD ID?")) {
            discountType = "PWD";
            ConsoleUtils.showInfo("PWD discount (20%) applied!");
        }

        // Payment
        String[] payMethods = {"E-Wallet", "Online Bank"};
        System.out.println("  Payment Methods:");
        for (int i = 0; i < payMethods.length; i++) System.out.printf("    [%d] %s%n", i+1, payMethods[i]);
        int payChoice = ConsoleUtils.getIntInput("Select payment", 1, 2);
        String paymentMethod = payMethods[payChoice - 1];

        if (!ConsoleUtils.confirm("Confirm reservation for seat " + seatNo + "?")) {
            ConsoleUtils.showInfo("Cancelled."); ConsoleUtils.pause(); return;
        }

        try {
            Transaction txn = reservationService.reserveSeat(
                customer.getCustomerNo(), screeningId, seatNo, "Online", paymentMethod, discountType, null);
            ConsoleUtils.showSuccess("Reservation confirmed!");
            System.out.println("\n  +-------------------------------------+");
            System.out.println("  |       BOOKING CONFIRMATION          |");
            System.out.println("  +-------------------------------------+");
            System.out.printf( "  |  Transaction: %-22s |\n", txn.getTransactionId());
            System.out.printf( "  |  Seat:        %-22s |\n", txn.getSeatNo());
            System.out.printf( "  |  Ticket:      PHP %-18.2f |\n", txn.getTicketPrice());
            System.out.printf( "  |  Booking Fee: PHP %-18.2f |\n", txn.getBookingFee());
            System.out.printf( "  |  Discount:    PHP %-18.2f |\n", txn.getDiscountAmount());
            System.out.printf( "  |  TOTAL:       PHP %-18.2f |\n", txn.getTotalPayment());
            System.out.println("  +-------------------------------------+");
        } catch (InvalidSeatException | DuplicateReservationException | PaymentFailedException e) {
            ConsoleUtils.showError(e.getMessage());
        }
        ConsoleUtils.pause();
    }


    private void viewMyReservations() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("MY RESERVATIONS");
        List<Transaction> list = reservationService.getCustomerReservations(customer.getCustomerNo());
        if (list.isEmpty()) { ConsoleUtils.showInfo("No reservations."); }
        else {
            System.out.println(Transaction.getTableDivider());
            System.out.println(Transaction.getTableHeader());
            System.out.println(Transaction.getTableDivider());
            for (Transaction t : list) System.out.println(t.toString());
            System.out.println(Transaction.getTableDivider());
            System.out.println("  Total: " + list.size());
        }
        ConsoleUtils.pause();
    }

    private void cancelReservation() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("CANCEL RESERVATION");
        List<Transaction> list = reservationService.getCustomerReservations(customer.getCustomerNo());
        if (list.isEmpty()) { ConsoleUtils.showInfo("No reservations."); ConsoleUtils.pause(); return; }
        System.out.println(Transaction.getTableDivider());
        System.out.println(Transaction.getTableHeader());
        System.out.println(Transaction.getTableDivider());
        for (Transaction t : list) {
            if (t.isConfirmed()) System.out.println(t.toString());
        }
        System.out.println(Transaction.getTableDivider());

        String txnId = ConsoleUtils.getInput("Transaction ID to cancel");
        if (ConsoleUtils.confirm("Cancel " + txnId + "?")) {
            boolean ok = reservationService.cancelReservation(txnId, customer.getCustomerNo());
            if (ok) ConsoleUtils.showSuccess("Reservation cancelled.");
            else ConsoleUtils.showError("Could not cancel.");
        }
        ConsoleUtils.pause();
    }
}
