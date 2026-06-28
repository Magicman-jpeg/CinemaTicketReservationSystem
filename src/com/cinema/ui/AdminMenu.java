package com.cinema.ui;

import com.cinema.dao.*;
import com.cinema.model.*;
import com.cinema.service.*;
import com.cinema.exception.DatabaseConnectionException;

import java.util.List;

/**
 * Console menu for Admin users.
 * Provides CRUD management for movies, screenings, customers, and reports.
 */
public class AdminMenu {

    private final Admin admin;
    private final MovieDAO movieDAO;
    private final ScreeningDAO screeningDAO;
    private final CustomerDAO customerDAO;
    private final CinemaSeatDAO seatDAO;
    private final TransactionDAO transactionDAO;
    private final ReportService reportService;

    public AdminMenu(Admin admin) {
        this.admin = admin;
        this.movieDAO = new MovieDAO();
        this.screeningDAO = new ScreeningDAO();
        this.customerDAO = new CustomerDAO();
        this.seatDAO = new CinemaSeatDAO();
        this.transactionDAO = new TransactionDAO();
        this.reportService = new ReportService();
    }

    /**
     * Main admin menu loop.
     */
    public void show() {
        boolean running = true;
        while (running) {
            String[] options = {
                "Manage Movies",
                "Manage Screenings",
                "Manage Customers",
                "View All Transactions",
                "Generate Reports",
                "View My Profile",
                "Logout"
            };
            int choice = ConsoleUtils.showMenu(
                "ADMIN PANEL - Welcome, " + admin.getFullName(), options);

            try {
                switch (choice) {
                    case 1 -> manageMovies();
                    case 2 -> manageScreenings();
                    case 3 -> manageCustomers();
                    case 4 -> viewTransactions();
                    case 5 -> generateReports();
                    case 6 -> System.out.println(admin.displayInfo());
                    case 7 -> running = false;
                }
            } catch (DatabaseConnectionException e) {
                ConsoleUtils.showError("Database error: " + e.getMessage());
            }
        }
        ConsoleUtils.showInfo("Logged out successfully.");
    }


    // ==================== MOVIE MANAGEMENT ====================

    private void manageMovies() throws DatabaseConnectionException {
        String[] options = {"Add Movie", "View All Movies", "Search Movie",
                           "Update Movie", "Delete Movie", "Back"};
        int choice = ConsoleUtils.showMenu("MOVIE MANAGEMENT", options);

        switch (choice) {
            case 1 -> addMovie();
            case 2 -> viewAllMovies();
            case 3 -> searchMovie();
            case 4 -> updateMovie();
            case 5 -> deleteMovie();
            case 6 -> { return; }
        }
    }

    private void addMovie() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ADD NEW MOVIE");
        String title = ConsoleUtils.getInput("Title");
        String genre = ConsoleUtils.getInput("Genre");
        int duration = ConsoleUtils.getIntInput("Duration (minutes)");
        String rating = ConsoleUtils.getInput("Rating (G/PG/PG-13/R)");
        String director = ConsoleUtils.getInput("Director");
        String releaseDate = ConsoleUtils.getInput("Release Date (YYYY-MM-DD)");

        Movie movie = new Movie(title, genre, duration, rating, director, releaseDate);
        int id = movieDAO.insert(movie);
        ConsoleUtils.showSuccess("Movie added with ID: " + id);
        ConsoleUtils.pause();
    }

    private void viewAllMovies() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL MOVIES");
        List<Movie> movies = movieDAO.findAll();
        if (movies.isEmpty()) {
            ConsoleUtils.showInfo("No movies found.");
        } else {
            System.out.println(Movie.getTableDivider());
            System.out.println(Movie.getTableHeader());
            System.out.println(Movie.getTableDivider());
            for (Movie m : movies) {
                System.out.println(m.toString());
            }
            System.out.println(Movie.getTableDivider());
            System.out.println("  Total: " + movies.size() + " movies");
        }
        ConsoleUtils.pause();
    }

    private void searchMovie() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("SEARCH MOVIE");
        String keyword = ConsoleUtils.getInput("Enter title keyword");
        List<Movie> movies = movieDAO.searchByTitle(keyword);
        if (movies.isEmpty()) {
            ConsoleUtils.showInfo("No movies found matching '" + keyword + "'.");
        } else {
            System.out.println(Movie.getTableDivider());
            System.out.println(Movie.getTableHeader());
            System.out.println(Movie.getTableDivider());
            for (Movie m : movies) {
                System.out.println(m.toString());
            }
            System.out.println(Movie.getTableDivider());
        }
        ConsoleUtils.pause();
    }


    private void updateMovie() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("UPDATE MOVIE");
        int id = ConsoleUtils.getIntInput("Enter Movie ID to update");
        Movie movie = movieDAO.findById(id);
        if (movie == null) {
            ConsoleUtils.showError("Movie not found with ID: " + id);
            ConsoleUtils.pause();
            return;
        }
        System.out.println("  Current: " + movie.getTitle());
        String title = ConsoleUtils.getInput("New Title (or press Enter to keep)");
        if (!title.isEmpty()) movie.setTitle(title);
        String genre = ConsoleUtils.getInput("New Genre (or Enter to keep)");
        if (!genre.isEmpty()) movie.setGenre(genre);
        String durStr = ConsoleUtils.getInput("New Duration in min (or Enter to keep)");
        if (!durStr.isEmpty()) movie.setDurationMinutes(Integer.parseInt(durStr));
        String rating = ConsoleUtils.getInput("New Rating (or Enter to keep)");
        if (!rating.isEmpty()) movie.setRating(rating);
        String director = ConsoleUtils.getInput("New Director (or Enter to keep)");
        if (!director.isEmpty()) movie.setDirector(director);

        movieDAO.update(movie);
        ConsoleUtils.showSuccess("Movie updated successfully.");
        ConsoleUtils.pause();
    }

    private void deleteMovie() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("DELETE MOVIE");
        int id = ConsoleUtils.getIntInput("Enter Movie ID to delete");
        Movie movie = movieDAO.findById(id);
        if (movie == null) {
            ConsoleUtils.showError("Movie not found with ID: " + id);
        } else {
            System.out.println("  Movie: " + movie.getTitle());
            if (ConsoleUtils.confirm("Are you sure you want to delete this movie?")) {
                movieDAO.delete(id);
                ConsoleUtils.showSuccess("Movie deleted.");
            } else {
                ConsoleUtils.showInfo("Deletion cancelled.");
            }
        }
        ConsoleUtils.pause();
    }

    // ==================== SCREENING MANAGEMENT ====================

    private void manageScreenings() throws DatabaseConnectionException {
        String[] options = {"Add Screening", "View All Screenings", "Update Screening",
                           "Delete Screening", "Back"};
        int choice = ConsoleUtils.showMenu("SCREENING MANAGEMENT", options);

        switch (choice) {
            case 1 -> addScreening();
            case 2 -> viewAllScreenings();
            case 3 -> updateScreening();
            case 4 -> deleteScreening();
            case 5 -> { return; }
        }
    }


    private void addScreening() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ADD NEW SCREENING");
        viewAllMovies();
        int movieId = ConsoleUtils.getIntInput("Movie ID");
        if (movieDAO.findById(movieId) == null) {
            ConsoleUtils.showError("Movie not found.");
            return;
        }
        String date = ConsoleUtils.getInput("Screen Date (YYYY-MM-DD)");
        String time = ConsoleUtils.getInput("Screen Time (HH:MM)");
        int hall = ConsoleUtils.getIntInput("Hall Number", 1, 5);
        double price = ConsoleUtils.getDoubleInput("Ticket Price (PHP)");

        Screening screening = new Screening(movieId, date, time, hall, price);
        int id = screeningDAO.insert(screening);
        // Generate seats for the new screening
        seatDAO.generateSeatsForScreening(id);
        ConsoleUtils.showSuccess("Screening added (ID: " + id + ") with 110 seats generated.");
        ConsoleUtils.pause();
    }

    private void viewAllScreenings() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL SCREENINGS");
        List<Screening> screenings = screeningDAO.findAll();
        if (screenings.isEmpty()) {
            ConsoleUtils.showInfo("No screenings found.");
        } else {
            System.out.println(Screening.getTableDivider());
            System.out.println(Screening.getTableHeader());
            System.out.println(Screening.getTableDivider());
            for (Screening s : screenings) {
                System.out.println(s.toString());
            }
            System.out.println(Screening.getTableDivider());
            System.out.println("  Total: " + screenings.size() + " screenings");
        }
    }

    private void updateScreening() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("UPDATE SCREENING");
        viewAllScreenings();
        int id = ConsoleUtils.getIntInput("Enter Screening ID to update");
        Screening s = screeningDAO.findById(id);
        if (s == null) {
            ConsoleUtils.showError("Screening not found.");
            ConsoleUtils.pause();
            return;
        }
        String date = ConsoleUtils.getInput("New Date (or Enter to keep)");
        if (!date.isEmpty()) s.setScreenDate(date);
        String time = ConsoleUtils.getInput("New Time (or Enter to keep)");
        if (!time.isEmpty()) s.setScreenTime(time);
        String priceStr = ConsoleUtils.getInput("New Price (or Enter to keep)");
        if (!priceStr.isEmpty()) s.setTicketPrice(Double.parseDouble(priceStr));

        screeningDAO.update(s);
        ConsoleUtils.showSuccess("Screening updated.");
        ConsoleUtils.pause();
    }

    private void deleteScreening() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("DELETE SCREENING");
        int id = ConsoleUtils.getIntInput("Enter Screening ID to delete");
        if (ConsoleUtils.confirm("Delete screening #" + id + "?")) {
            seatDAO.deleteByScreeningId(id);
            screeningDAO.delete(id);
            ConsoleUtils.showSuccess("Screening and its seats deleted.");
        }
        ConsoleUtils.pause();
    }


    // ==================== CUSTOMER MANAGEMENT ====================

    private void manageCustomers() throws DatabaseConnectionException {
        String[] options = {"View All Customers", "Search Customer", "Delete Customer", "Back"};
        int choice = ConsoleUtils.showMenu("CUSTOMER MANAGEMENT", options);

        switch (choice) {
            case 1 -> viewAllCustomers();
            case 2 -> searchCustomer();
            case 3 -> deleteCustomer();
            case 4 -> { return; }
        }
    }

    private void viewAllCustomers() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL CUSTOMERS");
        List<Customer> customers = customerDAO.findAll();
        if (customers.isEmpty()) {
            ConsoleUtils.showInfo("No customers found.");
        } else {
            System.out.println(Customer.getTableDivider());
            System.out.println(Customer.getTableHeader());
            System.out.println(Customer.getTableDivider());
            for (Customer c : customers) {
                System.out.println(c.toString());
            }
            System.out.println(Customer.getTableDivider());
            System.out.println("  Total: " + customers.size() + " customers");
        }
        ConsoleUtils.pause();
    }

    private void searchCustomer() throws DatabaseConnectionException {
        String keyword = ConsoleUtils.getInput("Enter customer name to search");
        List<Customer> customers = customerDAO.searchByName(keyword);
        if (customers.isEmpty()) {
            ConsoleUtils.showInfo("No customers found.");
        } else {
            for (Customer c : customers) {
                System.out.println(c.displayInfo());
            }
        }
        ConsoleUtils.pause();
    }

    private void deleteCustomer() throws DatabaseConnectionException {
        int id = ConsoleUtils.getIntInput("Enter Customer ID to delete");
        Customer c = customerDAO.findById(id);
        if (c == null) {
            ConsoleUtils.showError("Customer not found.");
        } else if (ConsoleUtils.confirm("Delete customer: " + c.getFullName() + "?")) {
            customerDAO.delete(id);
            ConsoleUtils.showSuccess("Customer deleted.");
        }
        ConsoleUtils.pause();
    }

    // ==================== TRANSACTIONS ====================

    private void viewTransactions() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL TRANSACTIONS");
        List<Transaction> transactions = transactionDAO.findAll();
        if (transactions.isEmpty()) {
            ConsoleUtils.showInfo("No transactions found.");
        } else {
            System.out.println(Transaction.getTableDivider());
            System.out.println(Transaction.getTableHeader());
            System.out.println(Transaction.getTableDivider());
            for (Transaction t : transactions) {
                System.out.println(t.toString());
            }
            System.out.println(Transaction.getTableDivider());
            System.out.println("  Total: " + transactions.size() + " transactions");
        }
        ConsoleUtils.pause();
    }

    // ==================== REPORTS ====================

    private void generateReports() throws DatabaseConnectionException {
        String[] options = {"Sales Summary", "Daily Revenue Report",
                           "Popular Movies Report", "Occupancy Report", "Back"};
        int choice = ConsoleUtils.showMenu("REPORT GENERATION", options);

        switch (choice) {
            case 1 -> System.out.println(reportService.generateSalesSummary());
            case 2 -> System.out.println(reportService.generateDailyRevenueReport());
            case 3 -> System.out.println(reportService.generatePopularMoviesReport());
            case 4 -> System.out.println(reportService.generateOccupancyReport());
            case 5 -> { return; }
        }
        ConsoleUtils.pause();
    }
}
