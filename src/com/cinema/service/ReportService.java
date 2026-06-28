package com.cinema.service;

import com.cinema.dao.*;
import com.cinema.exception.DatabaseConnectionException;
import com.cinema.model.*;
import java.util.List;
import java.util.Map;

/**
 * Service class for generating reports.
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

    public String generateSalesSummary() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("+=============================================================+\n");
        sb.append("|          CINEMA TICKET SALES SUMMARY REPORT                 |\n");
        sb.append("+=============================================================+\n");
        double totalRevenue = transactionDAO.getTotalRevenue();
        int totalTxn = transactionDAO.getCount();
        int totalMovies = movieDAO.getCount();
        int totalCustomers = customerDAO.getCount();
        int totalScreenings = screeningDAO.getCount();
        sb.append(String.format("|  Total Revenue:       PHP %,12.2f                 |\n", totalRevenue));
        sb.append(String.format("|  Total Transactions:  %,8d                         |\n", totalTxn));
        sb.append(String.format("|  Total Movies:        %,8d                         |\n", totalMovies));
        sb.append(String.format("|  Total Customers:     %,8d                         |\n", totalCustomers));
        sb.append(String.format("|  Total Screenings:    %,8d                         |\n", totalScreenings));
        sb.append("+=============================================================+\n");
        return sb.toString();
    }


    public String generateDailyRevenueReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+=============================================================+\n");
        sb.append("|                DAILY REVENUE REPORT                         |\n");
        sb.append("+=============================================================+\n");
        sb.append(String.format("| %-12s | %15s | %12s         |\n", "Date", "Revenue (PHP)", "Tickets"));
        sb.append("+==============================================================+\n");
        List<Map<String, String>> data = transactionDAO.getRevenueByDate();
        double total = 0; int totalTix = 0;
        for (Map<String, String> row : data) {
            double rev = parseDouble(row.get("daily_revenue"));
            int tix = parseInt(row.get("ticket_count"));
            total += rev; totalTix += tix;
            sb.append(String.format("| %-12s | %,15.2f | %,12d         |\n",
                    row.get("transaction_date"), rev, tix));
        }
        sb.append("+==============================================================+\n");
        sb.append(String.format("| %-12s | %,15.2f | %,12d         |\n", "TOTAL", total, totalTix));
        sb.append("+=============================================================+\n");
        return sb.toString();
    }

    public String generatePopularMoviesReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+=======================================================================+\n");
        sb.append("|              POPULAR MOVIES REPORT (By Ticket Sales)                   |\n");
        sb.append("+=======================================================================+\n");
        sb.append(String.format("| %-3s | %-32s | %7s | %13s  |\n", "#", "Movie Title", "Tickets", "Revenue (PHP)"));
        sb.append("+========================================================================+\n");
        List<Map<String, String>> data = transactionDAO.getPopularMovies();
        int rank = 1;
        for (Map<String, String> row : data) {
            String title = row.get("movie_title");
            if (title != null && title.length() > 32) title = title.substring(0, 29) + "...";
            sb.append(String.format("| %-3d | %-32s | %,7d | %,13.2f  |\n",
                    rank++, title, parseInt(row.get("tickets_sold")), parseDouble(row.get("total_revenue"))));
        }
        if (data.isEmpty()) sb.append("|              No sales data available.                                 |\n");
        sb.append("+=======================================================================+\n");
        return sb.toString();
    }

    public String generateOccupancyReport() throws DatabaseConnectionException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+===========================================================================+\n");
        sb.append("|                    SCREENING OCCUPANCY REPORT                              |\n");
        sb.append("+===========================================================================+\n");
        sb.append(String.format("| %-7s | %-10s | %-5s | %-6s | %-5s | %-5s | %-9s  |\n",
                "ID", "Date", "Time", "Cinema", "Avail", "Booked", "Occupancy"));
        sb.append("+===========================================================================+\n");
        List<Screening> screenings = screeningDAO.findAll();
        for (Screening s : screenings) {
            int avail = seatDAO.getAvailableCount(s.getScreeningId());
            int booked = 100 - avail;
            double occ = booked * 100.0 / 100;
            sb.append(String.format("| %-7s | %-10s | %-5s | %-6d | %-5d | %-5d | %7.1f%%   |\n",
                    s.getScreeningId(), s.getScreeningDate(), s.getTimeSlot(),
                    s.getCinemaNo(), avail, booked, occ));
        }
        sb.append("+===========================================================================+\n");
        return sb.toString();
    }

    private int parseInt(String v) { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } }
    private double parseDouble(String v) { try { return Double.parseDouble(v); } catch (Exception e) { return 0.0; } }
}
