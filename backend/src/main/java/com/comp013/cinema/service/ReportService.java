package com.comp013.cinema.service;

import com.comp013.cinema.exception.DatabaseConnectionException;
import com.comp013.cinema.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportService {

    public String generateConsoleSummary() throws DatabaseConnectionException, SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Tickets Sold Per Movie ===\n");
        String perMovie = """
                SELECT m.Movie_Title, COUNT(t.Transaction_ID) AS sold
                FROM [transaction] t
                JOIN movie m ON m.Movie_ID = t.Movie_ID
                WHERE t.Payment_Status='Paid'
                GROUP BY m.Movie_Title
                ORDER BY sold DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(perMovie);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append(rs.getString("Movie_Title")).append(": ").append(rs.getInt("sold")).append('\n');
            }
        }

        sb.append("\n=== Daily Revenue ===\n");
        String daily = """
                SELECT Transaction_Date, ROUND(SUM(Total_Payment),2) AS revenue
                FROM [transaction]
                GROUP BY Transaction_Date
                ORDER BY Transaction_Date
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(daily);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append(rs.getString("Transaction_Date")).append(": ").append(rs.getDouble("revenue")).append('\n');
            }
        }
        return sb.toString();
    }
}
