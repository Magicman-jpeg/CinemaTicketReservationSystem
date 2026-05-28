package com.comp013.cinema.service;

import com.comp013.cinema.exception.DatabaseConnectionException;
import com.comp013.cinema.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CrudService {

    public void addMovie(int movieId, String title, String duration, String releaseDate) throws DatabaseConnectionException, SQLException {
        String sql = "INSERT INTO movie (Movie_ID, Movie_Title, Genre_ID, Movie_Duration, Duration_Code, Release_Date, Age_Rate_ID, Status_ID) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setString(2, title);
            ps.setInt(3, 1);
            ps.setString(4, duration);
            ps.setInt(5, 120);
            ps.setString(6, releaseDate);
            ps.setInt(7, 1);
            ps.setInt(8, 1);
            ps.executeUpdate();
        }
    }

    public ResultSet searchMovieByTitle(Connection conn, String keyword) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM movie WHERE Movie_Title LIKE ?");
        ps.setString(1, "%" + keyword + "%");
        return ps.executeQuery();
    }

    public int updateMovieTitle(int movieId, String newTitle) throws DatabaseConnectionException, SQLException {
        String sql = "UPDATE movie SET Movie_Title=? WHERE Movie_ID=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setInt(2, movieId);
            return ps.executeUpdate();
        }
    }

    public int deleteMovie(int movieId) throws DatabaseConnectionException, SQLException {
        String sql = "DELETE FROM movie WHERE Movie_ID=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            return ps.executeUpdate();
        }
    }

    public ResultSet viewTable(Connection conn, String tableName) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT * FROM [" + tableName + "]");
    }

    public void addCustomer(int customerNo, String name, int age) throws DatabaseConnectionException, SQLException {
        String sql = "INSERT INTO customer (Customer_No, Name, Age, PWD, Customer_Type, [App User]) VALUES (?, ?, ?, 'No', 'Regular', 'No')";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerNo);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.executeUpdate();
        }
    }

    public int updateCustomerName(int customerNo, String name) throws DatabaseConnectionException, SQLException {
        String sql = "UPDATE customer SET Name=? WHERE Customer_No=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, customerNo);
            return ps.executeUpdate();
        }
    }

    public int deleteCustomer(int customerNo) throws DatabaseConnectionException, SQLException {
        String sql = "DELETE FROM customer WHERE Customer_No=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerNo);
            return ps.executeUpdate();
        }
    }

    public void addScreening(String screeningId, String day, String date, String timeSlot, int movieId) throws DatabaseConnectionException, SQLException {
        String sql = "INSERT INTO screenings (Screening_ID, Screening_Day, Screening_Date, Time_Slot, Cinema_No, Seat_Type_ID, Movie_ID) VALUES (?, ?, ?, ?, 1, 1, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, screeningId);
            ps.setString(2, day);
            ps.setString(3, date);
            ps.setString(4, timeSlot);
            ps.setInt(5, movieId);
            ps.executeUpdate();
        }
    }

    public int deleteScreening(String screeningId) throws DatabaseConnectionException, SQLException {
        String sql = "DELETE FROM screenings WHERE Screening_ID=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, screeningId);
            return ps.executeUpdate();
        }
    }

    public int deleteReservation(String transactionId) throws DatabaseConnectionException, SQLException {
        String sql = "DELETE FROM [transaction] WHERE Transaction_ID=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            return ps.executeUpdate();
        }
    }
}
