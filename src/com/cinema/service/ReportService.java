package com.cinema.service;

import com.cinema.dao.*;
import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.*;

import java.util.List;
import java.util.Map;

/**
 * Service class for generating system reports.
 * Provides analytics on sales, movies, and reservations.
 */
public class ReportService {

    private final TransactionDAO transactionDAO;
    private final MovieDAO movieDAO;
    private final ScreeningDAO screeningDAO;
    private final CustomerDAO customerDAO;
    private final CinemaSeatDAO seatDAO;

    public ReportService() {
        this.transactionDAO = new TransactionDAO();
        this.movieDAO = new MovieDAO();
        this.screeningDAO = new ScreeningDAO();
        this.customerDAO = new CustomerDAO();
        this.seatDAO = new CinemaSeatDAO();
    }

    /**
     * Generates a sales summary report.
     */
    public String generateSalesSummary() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║              CINEMA TICKET SALES SUMMARY REPORT             ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        double totalRevenue = transactionDAO.getTotalRevenue();
        int totalTransactions = transactionDAO.getCount();
        int totalMovies = movieDAO.getCount();
        int totalCustomers = customerDAO.getCount();

        sb.append(String.format("║  Total Revenue:        PHP %,12.2f                  ║\n", totalRevenue));
        sb.append(String.format("║  Total Transactions:   %,8d                            ║\n", totalTransactions));
        sb.append(String.format("║  Total Movies:         %,8d                            ║\n", totalMovies));
        sb.append(String.format("║  Total Customers:      %,8d                            ║\n", totalCustomers));
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }


    /**
     * Generates daily revenue report.
     */
    public String generateDailyRevenueReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║                  DAILY REVENUE REPORT                       ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ %-12s │ %15s │ %12s          ║\n",
                "Date", "Revenue (PHP)", "Tickets"));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");

        List<Map<String, String>> data = transactionDAO.getRevenueByDate();
        double grandTotal = 0;
        int totalTickets = 0;

        for (Map<String, String> row : data) {
            String date = row.get("transaction_date");
            double revenue = parseDoubleSafe(row.get("daily_revenue"));
            int tickets = parseIntSafe(row.get("ticket_count"));
            grandTotal += revenue;
            totalTickets += tickets;
            sb.append(String.format("║ %-12s │ %,15.2f │ %,12d          ║\n",
                    date, revenue, tickets));
        }

        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ %-12s │ %,15.2f │ %,12d          ║\n",
                "TOTAL", grandTotal, totalTickets));
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }

    /**
     * Generates popular movies report (by ticket sales).
     */
    public String generatePopularMoviesReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                   POPULAR MOVIES REPORT (By Ticket Sales)              ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ %-3s │ %-30s │ %8s │ %15s   ║\n",
                "#", "Movie Title", "Tickets", "Revenue (PHP)"));
        sb.append("╠══════════════════════════════════════════════════════════════════════════╣\n");

        List<Map<String, String>> data = transactionDAO.getPopularMovies();
        int rank = 1;
        for (Map<String, String> row : data) {
            String title = row.get("title");
            if (title != null && title.length() > 30) title = title.substring(0, 27) + "...";
            int tickets = parseIntSafe(row.get("tickets_sold"));
            double revenue = parseDoubleSafe(row.get("total_revenue"));
            sb.append(String.format("║ %-3d │ %-30s │ %,8d │ %,15.2f   ║\n",
                    rank++, title, tickets, revenue));
        }

        if (data.isEmpty()) {
            sb.append("║                       No sales data available.                         ║\n");
        }
        sb.append("╚══════════════════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }


    /**
     * Generates a screening occupancy report.
     */
    public String generateOccupancyReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("╔══════════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                       SCREENING OCCUPANCY REPORT                               ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ %-4s │ %-10s │ %-8s │ %-6s │ %-9s │ %-9s │ %-8s    ║\n",
                "ID", "Date", "Time", "Hall", "Available", "Reserved", "Occupancy"));
        sb.append("╠══════════════════════════════════════════════════════════════════════════════════╣\n");

        List<Screening> screenings = screeningDAO.findAll();
        for (Screening s : screenings) {
            int available = seatDAO.getAvailableCount(s.getScreeningId());
            int totalSeats = 110; // 11 rows x 10 columns
            int reserved = totalSeats - available;
            double occupancy = (reserved * 100.0) / totalSeats;

            sb.append(String.format("║ %-4d │ %-10s │ %-8s │ %-6d │ %-9d │ %-9d │ %6.1f%%     ║\n",
                    s.getScreeningId(), s.getScreenDate(), s.getScreenTime(),
                    s.getHallNumber(), available, reserved, occupancy));
        }

        if (screenings.isEmpty()) {
            sb.append("║                         No screenings found.                                  ║\n");
        }
        sb.append("╚══════════════════════════════════════════════════════════════════════════════════╝\n");

        return sb.toString();
    }

    /**
     * Generates all movies listing report.
     */
    public String generateMovieListingReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(Movie.getTableDivider()).append("\n");
        sb.append(Movie.getTableHeader()).append("\n");
        sb.append(Movie.getTableDivider()).append("\n");

        List<Movie> movies = movieDAO.findAll();
        for (Movie m : movies) {
            sb.append(m.toString()).append("\n");
        }
        sb.append(Movie.getTableDivider()).append("\n");
        sb.append("Total Movies: ").append(movies.size()).append("\n");

        return sb.toString();
    }

    private int parseIntSafe(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }

    private double parseDoubleSafe(String value) {
        try { return Double.parseDouble(value); }
        catch (Exception e) { return 0.0; }
    }
}
