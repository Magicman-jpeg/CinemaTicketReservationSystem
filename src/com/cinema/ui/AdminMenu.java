package com.cinema.ui;

import com.cinema.dao.*;
import com.cinema.model.*;
import com.cinema.service.*;
import com.cinema.exception.DatabaseConnectionException;
import java.util.List;
import java.util.Map;

/**
 * Console menu for Admin users.
 */
public class AdminMenu {

    private final Admin admin;
    private final MovieDAO movieDAO;
    private final ScreeningDAO screeningDAO;
    private final CustomerDAO customerDAO;
    private final TransactionDAO transactionDAO;
    private final ReportService reportService;

    public AdminMenu(Admin admin) {
        this.admin = admin;
        this.movieDAO = new MovieDAO();
        this.screeningDAO = new ScreeningDAO();
        this.customerDAO = new CustomerDAO();
        this.transactionDAO = new TransactionDAO();
        this.reportService = new ReportService();
    }

    public void show() {
        boolean running = true;
        while (running) {
            String[] options = { "Manage Movies", "Manage Screenings",
                "Manage Customers", "View All Transactions",
                "Generate Reports", "View My Profile", "Logout" };
            int choice = ConsoleUtils.showMenu(
                "ADMIN PANEL - Welcome, " + admin.getAdminName(), options);
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
        ConsoleUtils.showInfo("Logged out.");
    }


    private void manageMovies() throws DatabaseConnectionException {
        String[] opts = {"View All Movies", "Search Movie", "Add Movie", "Update Movie", "Delete Movie", "Back"};
        int ch = ConsoleUtils.showMenu("MOVIE MANAGEMENT", opts);
        switch (ch) {
            case 1 -> viewMovies();
            case 2 -> searchMovie();
            case 3 -> addMovie();
            case 4 -> updateMovie();
            case 5 -> deleteMovie();
        }
    }

    private void viewMovies() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL MOVIES");
        List<Movie> movies = movieDAO.findAll();
        System.out.println(Movie.getTableDivider());
        System.out.println(Movie.getTableHeader());
        System.out.println(Movie.getTableDivider());
        for (Movie m : movies) System.out.println(m.toString());
        System.out.println(Movie.getTableDivider());
        System.out.println("  Total: " + movies.size() + " movies");
        ConsoleUtils.pause();
    }

    private void searchMovie() throws DatabaseConnectionException {
        String kw = ConsoleUtils.getInput("Enter title keyword");
        List<Movie> movies = movieDAO.searchByTitle(kw);
        if (movies.isEmpty()) { ConsoleUtils.showInfo("No matches."); }
        else { for (Movie m : movies) System.out.println(m.toString()); }
        ConsoleUtils.pause();
    }

    private void addMovie() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ADD MOVIE");
        String title = ConsoleUtils.getInput("Title");
        // Show genres
        List<Map<String,String>> genres = movieDAO.getGenres();
        System.out.println("  Genres: ");
        for (Map<String,String> g : genres) System.out.println("    " + g.get("genre_id") + " - " + g.get("movie_genre"));
        int genreId = ConsoleUtils.getIntInput("Genre ID", 1, 9);
        String duration = ConsoleUtils.getInput("Duration (e.g. '2 hrs 07 mins')");
        int durCode = ConsoleUtils.getIntInput("Duration code (minutes total)");
        String release = ConsoleUtils.getInput("Release Date (YYYY-MM-DD)");
        int statusId = ConsoleUtils.getIntInput("Status (1=Upcoming, 2=Showing, 3=Post-Screening)", 1, 3);
        int ageId = ConsoleUtils.getIntInput("Age Rating (1=PG, 2=R13, 3=R16)", 1, 3);
        Movie m = new Movie(0, title, genreId, duration, durCode, release, statusId, ageId);
        int id = movieDAO.insert(m);
        ConsoleUtils.showSuccess("Movie added (ID: " + id + ")");
        ConsoleUtils.pause();
    }

    private void updateMovie() throws DatabaseConnectionException {
        int id = ConsoleUtils.getIntInput("Movie ID to update");
        Movie m = movieDAO.findById(id);
        if (m == null) { ConsoleUtils.showError("Not found."); ConsoleUtils.pause(); return; }
        System.out.println("  Current: " + m.getMovieTitle());
        String t = ConsoleUtils.getInput("New Title (Enter to keep)");
        if (!t.isEmpty()) m.setMovieTitle(t);
        String s = ConsoleUtils.getInput("New Status (1/2/3 or Enter)");
        if (!s.isEmpty()) m.setStatusId(Integer.parseInt(s));
        movieDAO.update(m);
        ConsoleUtils.showSuccess("Updated."); ConsoleUtils.pause();
    }

    private void deleteMovie() throws DatabaseConnectionException {
        int id = ConsoleUtils.getIntInput("Movie ID to delete");
        if (ConsoleUtils.confirm("Delete movie " + id + "?")) {
            movieDAO.delete(id); ConsoleUtils.showSuccess("Deleted.");
        }
        ConsoleUtils.pause();
    }


    private void manageScreenings() throws DatabaseConnectionException {
        String[] opts = {"View All Screenings", "Add Screening", "Delete Screening", "Back"};
        int ch = ConsoleUtils.showMenu("SCREENING MANAGEMENT", opts);
        switch (ch) {
            case 1 -> viewScreenings();
            case 2 -> addScreening();
            case 3 -> deleteScreening();
        }
    }

    private void viewScreenings() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL SCREENINGS");
        List<Screening> list = screeningDAO.findAll();
        System.out.println(Screening.getTableDivider());
        System.out.println(Screening.getTableHeader());
        System.out.println(Screening.getTableDivider());
        for (Screening s : list) System.out.println(s.toString());
        System.out.println(Screening.getTableDivider());
        System.out.println("  Total: " + list.size()); ConsoleUtils.pause();
    }

    private void addScreening() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ADD SCREENING");
        viewMovies();
        String id = ConsoleUtils.getInput("Screening ID (e.g. WED-1)");
        String day = ConsoleUtils.getInput("Day (SUN/MON/TUE/WED/etc)");
        String date = ConsoleUtils.getInput("Date (YYYY-MM-DD)");
        String time = ConsoleUtils.getInput("Time Slot (HH:MM)");
        int seatType = ConsoleUtils.getIntInput("Seat Type (1=VIP, 2=Premium, 3=Regular)", 1, 3);
        int movieId = ConsoleUtils.getIntInput("Movie ID");
        int cinema = ConsoleUtils.getIntInput("Cinema No", 1, 5);
        Screening s = new Screening(id, day, date, time, seatType, movieId, cinema);
        screeningDAO.insert(s);
        ConsoleUtils.showSuccess("Screening added: " + id); ConsoleUtils.pause();
    }

    private void deleteScreening() throws DatabaseConnectionException {
        String id = ConsoleUtils.getInput("Screening ID to delete");
        if (ConsoleUtils.confirm("Delete " + id + "?")) {
            screeningDAO.delete(id); ConsoleUtils.showSuccess("Deleted.");
        }
        ConsoleUtils.pause();
    }

    private void manageCustomers() throws DatabaseConnectionException {
        String[] opts = {"View All Customers", "Search Customer", "Delete Customer", "Back"};
        int ch = ConsoleUtils.showMenu("CUSTOMER MANAGEMENT", opts);
        switch (ch) {
            case 1 -> { viewCustomers(); ConsoleUtils.pause(); }
            case 2 -> { searchCustomer(); ConsoleUtils.pause(); }
            case 3 -> { deleteCustomer(); ConsoleUtils.pause(); }
        }
    }

    private void viewCustomers() throws DatabaseConnectionException {
        List<Customer> list = customerDAO.findAll();
        System.out.println(Customer.getTableDivider());
        System.out.println(Customer.getTableHeader());
        System.out.println(Customer.getTableDivider());
        for (Customer c : list) System.out.println(c.toString());
        System.out.println(Customer.getTableDivider());
        System.out.println("  Total: " + list.size());
    }

    private void searchCustomer() throws DatabaseConnectionException {
        String kw = ConsoleUtils.getInput("Search name");
        List<Customer> list = customerDAO.searchByName(kw);
        if (list.isEmpty()) ConsoleUtils.showInfo("No matches.");
        else for (Customer c : list) System.out.println(c.displayInfo());
    }

    private void deleteCustomer() throws DatabaseConnectionException {
        int id = ConsoleUtils.getIntInput("Customer No to delete");
        if (ConsoleUtils.confirm("Delete customer " + id + "?")) {
            customerDAO.delete(id); ConsoleUtils.showSuccess("Deleted.");
        }
    }

    private void viewTransactions() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ALL TRANSACTIONS");
        List<Transaction> list = transactionDAO.findAll();
        System.out.println(Transaction.getTableDivider());
        System.out.println(Transaction.getTableHeader());
        System.out.println(Transaction.getTableDivider());
        for (Transaction t : list) System.out.println(t.toString());
        System.out.println(Transaction.getTableDivider());
        System.out.println("  Total: " + list.size()); ConsoleUtils.pause();
    }

    private void generateReports() throws DatabaseConnectionException {
        String[] opts = {"Sales Summary", "Daily Revenue", "Popular Movies", "Occupancy Report", "Back"};
        int ch = ConsoleUtils.showMenu("REPORTS", opts);
        switch (ch) {
            case 1 -> System.out.println(reportService.generateSalesSummary());
            case 2 -> System.out.println(reportService.generateDailyRevenueReport());
            case 3 -> System.out.println(reportService.generatePopularMoviesReport());
            case 4 -> System.out.println(reportService.generateOccupancyReport());
        }
        if (ch < 5) ConsoleUtils.pause();
    }
}
