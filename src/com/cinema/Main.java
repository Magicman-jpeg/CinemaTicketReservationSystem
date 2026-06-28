package com.cinema;

import com.cinema.dao.DatabaseManager;
import com.cinema.exception.DatabaseConnectionException;
import com.cinema.exception.InvalidLoginException;
import com.cinema.model.Admin;
import com.cinema.model.Customer;
import com.cinema.service.LoginService;
import com.cinema.ui.AdminMenu;
import com.cinema.ui.ConsoleUtils;
import com.cinema.ui.CustomerMenu;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main entry point for the Cinema Ticket Reservation System.
 * COMP 013 - OOP Final Project - Group 2
 * Data source: EDITED GROUP 2_cinema_hall_ticket_sales.xlsx
 */
public class Main {

    private static final LoginService loginService = new LoginService();

    public static void main(String[] args) {
        try {
            initializeDatabase();
            runApplication();
        } catch (DatabaseConnectionException e) {
            ConsoleUtils.showError("Fatal: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void initializeDatabase() throws DatabaseConnectionException {
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.testConnection() && db.tableExists("movie")) {
            ConsoleUtils.showInfo("Database loaded.");
            return;
        }
        ConsoleUtils.showInfo("Initializing database...");
        String schema = System.getProperty("user.dir") + "/db/schema.sql";
        String data = System.getProperty("user.dir") + "/db/sample_data.sql";
        if (Files.exists(Paths.get(schema))) {
            db.executeScript(schema);
            ConsoleUtils.showSuccess("Schema created.");
        } else {
            throw new DatabaseConnectionException("schema.sql not found", "INIT");
        }
        if (Files.exists(Paths.get(data))) {
            db.executeScript(data);
            ConsoleUtils.showSuccess("Sample data loaded.");
        }
    }


    private static void runApplication() {
        boolean running = true;
        while (running) {
            ConsoleUtils.showBanner();
            String[] options = { "Login as Admin", "Login as Customer",
                "Register New Customer", "Exit System" };
            int choice = ConsoleUtils.showMenu("MAIN MENU", options);
            try {
                switch (choice) {
                    case 1 -> loginAdmin();
                    case 2 -> loginCustomer();
                    case 3 -> registerCustomer();
                    case 4 -> {
                        running = false;
                        System.out.println("\n  +-------------------------------------------+");
                        System.out.println("  |   Thank you for using the Cinema System!  |");
                        System.out.println("  |   COMP 013 - Group 2 Final Project        |");
                        System.out.println("  +-------------------------------------------+\n");
                    }
                }
            } catch (DatabaseConnectionException e) {
                ConsoleUtils.showError("Database error: " + e.getMessage());
            }
        }
    }

    private static void loginAdmin() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ADMIN LOGIN");
        String user = ConsoleUtils.getInput("Username");
        String pass = ConsoleUtils.getInput("Password");
        try {
            Admin admin = loginService.loginAdmin(user, pass);
            ConsoleUtils.showSuccess("Welcome, " + admin.getAdminName() + "!");
            ConsoleUtils.pause();
            new AdminMenu(admin).show();
        } catch (InvalidLoginException e) {
            ConsoleUtils.showError(e.getMessage()); ConsoleUtils.pause();
        }
    }

    private static void loginCustomer() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("CUSTOMER LOGIN");
        String user = ConsoleUtils.getInput("Username");
        String pass = ConsoleUtils.getInput("Password");
        try {
            Customer customer = loginService.loginCustomer(user, pass);
            ConsoleUtils.showSuccess("Welcome, " + customer.getName() + "!");
            ConsoleUtils.pause();
            new CustomerMenu(customer).show();
        } catch (InvalidLoginException e) {
            ConsoleUtils.showError(e.getMessage()); ConsoleUtils.pause();
        }
    }

    private static void registerCustomer() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("CUSTOMER REGISTRATION");
        String user = ConsoleUtils.getInput("Choose username");
        String pass = ConsoleUtils.getInput("Choose password");
        String name = ConsoleUtils.getInput("Full Name");
        int age = ConsoleUtils.getIntInput("Age");
        String email = ConsoleUtils.getInput("Email");
        String mobile = ConsoleUtils.getInput("Mobile No");
        Customer c = loginService.registerCustomer(user, pass, name, age, email, mobile);
        if (c != null) {
            ConsoleUtils.showSuccess("Registered! Customer No: " + c.getCustomerNo());
        } else {
            ConsoleUtils.showError("Username already taken.");
        }
        ConsoleUtils.pause();
    }
}
