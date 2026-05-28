package com.comp013.cinema.dao;

import com.comp013.cinema.exception.DatabaseConnectionException;
import com.comp013.cinema.model.Admin;
import com.comp013.cinema.model.Customer;
import com.comp013.cinema.model.Movie;
import com.comp013.cinema.model.Screening;
import com.comp013.cinema.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaDao {

    public Admin findAdmin(String username, String password) throws DatabaseConnectionException, SQLException {
        String sql = "SELECT Admin_ID, Admin_Name FROM admin WHERE Admin_Username=? AND [Admin _Pass]=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("Admin_ID"), username, password, rs.getString("Admin_Name"));
                }
                return null;
            }
        }
    }

    public Customer findCustomer(String username, String password) throws DatabaseConnectionException, SQLException {
        String sql = "SELECT Customer_No, Name FROM customer WHERE Customer_Username=? AND Customer_Pass=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(rs.getInt("Customer_No"), username, password, rs.getString("Name"));
                }
                return null;
            }
        }
    }

    public List<Movie> listMovies() throws DatabaseConnectionException, SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT Movie_ID, Movie_Title, Movie_Duration, Release_Date FROM movie ORDER BY Movie_ID";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("Movie_ID"), rs.getString("Movie_Title"),
                        rs.getString("Movie_Duration"), rs.getString("Release_Date")));
            }
        }
        return movies;
    }

    public List<Screening> listScreenings() throws DatabaseConnectionException, SQLException {
        List<Screening> screenings = new ArrayList<>();
        String sql = "SELECT Screening_ID, Screening_Date, Time_Slot, Movie_ID FROM screenings ORDER BY Screening_Date, Time_Slot";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                screenings.add(new Screening(rs.getString("Screening_ID"),
                        rs.getString("Screening_Date"),
                        rs.getString("Time_Slot"),
                        rs.getInt("Movie_ID")));
            }
        }
        return screenings;
    }

    public boolean seatExists(String seatNo) throws DatabaseConnectionException, SQLException {
        String sql = "SELECT COUNT(*) FROM cinema_seat WHERE Seat_No=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seatNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean seatBookedForScreening(String seatNo, String screeningId) throws DatabaseConnectionException, SQLException {
        String sql = "SELECT COUNT(*) FROM [transaction] WHERE Seat_No=? AND Screening_ID=? AND Payment_Status='Paid'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seatNo);
            ps.setString(2, screeningId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void insertTransaction(String transactionId, int customerNo, String seatNo, String screeningId, int movieId,
                                  double ticketPrice, double discountAmount, double totalPayment, String paymentMethod,
                                  String discountType) throws DatabaseConnectionException, SQLException {
        String sql = """
                INSERT INTO [transaction]
                (Transaction_ID, Transaction_Date, Transaction_Time, Customer_No, Seat_No, Screening_ID, Seat_Type_ID,
                 Movie_ID, Reservation_Type, Admin_ID, Booking_Fee, Ticket_Price, Discount_Type, Discount_Amount,
                 Payment_Method, Total_Payment, Payment_Status)
                VALUES (?, date('now'), time('now'), ?, ?, ?, 1, ?, 'Online', 'N/A', 20, ?, ?, ?, ?, ?, 'Paid')
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.setInt(2, customerNo);
            ps.setString(3, seatNo);
            ps.setString(4, screeningId);
            ps.setInt(5, movieId);
            ps.setDouble(6, ticketPrice);
            ps.setString(7, discountType);
            ps.setDouble(8, discountAmount);
            ps.setString(9, paymentMethod);
            ps.setDouble(10, totalPayment);
            ps.executeUpdate();
        }
    }
}
