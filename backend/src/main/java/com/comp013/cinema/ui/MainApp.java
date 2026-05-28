package com.comp013.cinema.ui;

import com.comp013.cinema.dao.CinemaDao;
import com.comp013.cinema.exception.InvalidLoginException;
import com.comp013.cinema.model.Customer;
import com.comp013.cinema.model.Movie;
import com.comp013.cinema.model.Screening;
import com.comp013.cinema.model.User;
import com.comp013.cinema.service.AuthService;
import com.comp013.cinema.service.CrudService;
import com.comp013.cinema.service.ReportService;
import com.comp013.cinema.service.ReservationService;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    private final Scanner scanner = new Scanner(System.in);
    private final CinemaDao dao = new CinemaDao();
    private final AuthService authService = new AuthService(dao);
    private final ReservationService reservationService = new ReservationService(dao);
    private final CrudService crudService = new CrudService();
    private final ReportService reportService = new ReportService();

    public static void main(String[] args) {
        new MainApp().start();
    }

    private void start() {
        try {
            User user = loginFlow();
            if ("ADMIN".equals(user.getRole())) {
                runAdminMenu();
            } else {
                runCustomerMenu((Customer) user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private User loginFlow() throws Exception {
        System.out.println("=== CINEMA TICKET RESERVATION SYSTEM ===");
        System.out.println("1. Console Login");
        System.out.println("2. JOptionPane Login");
        System.out.print("Select mode: ");
        String mode = scanner.nextLine();
        String username;
        String password;
        if ("2".equals(mode)) {
            username = JOptionPane.showInputDialog(null, "Username:");
            password = JOptionPane.showInputDialog(null, "Password:");
        } else {
            System.out.print("Username: ");
            username = scanner.nextLine();
            System.out.print("Password: ");
            password = scanner.nextLine();
        }
        try {
            User user = authService.login(username, password);
            System.out.println("Welcome " + user.getName() + " (" + user.getRole() + ")");
            return user;
        } catch (InvalidLoginException e) {
            System.out.println("Invalid login. Please try again.");
            throw e;
        }
    }

    private void runAdminMenu() {
        while (true) {
            try {
                System.out.println("\n--- ADMIN MENU ---");
                System.out.println("1. View movies");
                System.out.println("2. Add movie");
                System.out.println("3. Search movie");
                System.out.println("4. Update movie title");
                System.out.println("5. Delete movie");
                System.out.println("6. View screenings");
                System.out.println("7. Add customer");
                System.out.println("8. Update customer name");
                System.out.println("9. Delete customer");
                System.out.println("10. Add screening");
                System.out.println("11. Delete screening");
                System.out.println("12. Delete reservation");
                System.out.println("13. Generate report");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> dao.listMovies().forEach(m -> System.out.println(m.getMovieId() + " | " + m.getTitle()));
                    case "2" -> {
                        System.out.print("Movie ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Duration (e.g. 2 hrs): ");
                        String duration = scanner.nextLine();
                        System.out.print("Release date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        crudService.addMovie(id, title, duration, date);
                        System.out.println("Movie added.");
                    }
                    case "3" -> {
                        System.out.print("Keyword: ");
                        String kw = scanner.nextLine();
                        dao.listMovies().stream().filter(m -> m.getTitle().toLowerCase().contains(kw.toLowerCase()))
                                .forEach(m -> System.out.println(m.getMovieId() + " | " + m.getTitle()));
                    }
                    case "4" -> {
                        System.out.print("Movie ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("New title: ");
                        String newTitle = scanner.nextLine();
                        int updated = crudService.updateMovieTitle(id, newTitle);
                        System.out.println(updated > 0 ? "Updated." : "Movie not found.");
                    }
                    case "5" -> {
                        System.out.print("Movie ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        int deleted = crudService.deleteMovie(id);
                        System.out.println(deleted > 0 ? "Deleted." : "Movie not found.");
                    }
                    case "6" -> dao.listScreenings().forEach(s ->
                            System.out.println(s.getScreeningId() + " | " + s.getScreeningDate() + " " + s.getTimeSlot() + " | Movie " + s.getMovieId()));
                    case "7" -> {
                        System.out.print("Customer No: ");
                        int customerNo = Integer.parseInt(scanner.nextLine());
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Age: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        crudService.addCustomer(customerNo, name, age);
                        System.out.println("Customer added.");
                    }
                    case "8" -> {
                        System.out.print("Customer No: ");
                        int customerNo = Integer.parseInt(scanner.nextLine());
                        System.out.print("New Name: ");
                        String name = scanner.nextLine();
                        int updated = crudService.updateCustomerName(customerNo, name);
                        System.out.println(updated > 0 ? "Updated." : "Customer not found.");
                    }
                    case "9" -> {
                        System.out.print("Customer No: ");
                        int customerNo = Integer.parseInt(scanner.nextLine());
                        int deleted = crudService.deleteCustomer(customerNo);
                        System.out.println(deleted > 0 ? "Deleted." : "Customer not found.");
                    }
                    case "10" -> {
                        System.out.print("Screening ID: ");
                        String screeningId = scanner.nextLine();
                        System.out.print("Day (e.g. MON): ");
                        String day = scanner.nextLine();
                        System.out.print("Date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        System.out.print("Time (HH:MM:SS): ");
                        String time = scanner.nextLine();
                        System.out.print("Movie ID: ");
                        int movieId = Integer.parseInt(scanner.nextLine());
                        crudService.addScreening(screeningId, day, date, time, movieId);
                        System.out.println("Screening added.");
                    }
                    case "11" -> {
                        System.out.print("Screening ID: ");
                        String screeningId = scanner.nextLine();
                        int deleted = crudService.deleteScreening(screeningId);
                        System.out.println(deleted > 0 ? "Deleted." : "Screening not found.");
                    }
                    case "12" -> {
                        System.out.print("Transaction ID: ");
                        String txId = scanner.nextLine();
                        int deleted = crudService.deleteReservation(txId);
                        System.out.println(deleted > 0 ? "Deleted." : "Reservation not found.");
                    }
                    case "13" -> System.out.println(reportService.generateConsoleSummary());
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void runCustomerMenu(Customer customer) {
        while (true) {
            try {
                System.out.println("\n--- CUSTOMER MENU ---");
                System.out.println("1. View movies");
                System.out.println("2. View screenings");
                System.out.println("3. Reserve seat");
                System.out.println("4. Check my tickets");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        List<Movie> movies = dao.listMovies();
                        movies.forEach(m -> System.out.println(m.getMovieId() + " | " + m.getTitle()));
                    }
                    case "2" -> {
                        List<Screening> screenings = dao.listScreenings();
                        screenings.forEach(s -> System.out.println(s.getScreeningId() + " | " + s.getScreeningDate() + " " + s.getTimeSlot()));
                    }
                    case "3" -> {
                        System.out.print("Seat No (A1-K10): ");
                        String seatNo = scanner.nextLine().toUpperCase();
                        System.out.print("Screening ID: ");
                        String screeningId = scanner.nextLine();
                        System.out.print("Movie ID: ");
                        int movieId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ticket Price: ");
                        double ticketPrice = Double.parseDouble(scanner.nextLine());
                        System.out.print("Discount Type (N/A or Senior Citizen): ");
                        String discountType = scanner.nextLine();
                        System.out.print("Payment Method: ");
                        String paymentMethod = scanner.nextLine();
                        reservationService.reserveSeat(customer, seatNo, screeningId, movieId, ticketPrice, discountType, paymentMethod);
                        System.out.println("Reservation completed.");
                    }
                    case "4" -> System.out.println("Open frontend/report.html to view summary report.");
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
}
