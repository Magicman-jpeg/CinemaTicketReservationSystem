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
 * COMP 013 - Object-Oriented Programming Final Project
 *
 * OOP Concepts Demonstrated:
 * - Encapsulation: Private fields with public getters/setters in all models
 * - Inheritance: User -> Admin, Customer hierarchy
 * - Polymorphism: User.displayInfo() overridden in Admin and Customer
 * - Abstraction: Abstract User class defines contract for subclasses
 * - Exception Handling: Custom exceptions for business logic validation
 * - Constructors: Default, parameterized, and overloaded constructors
 * - Methods: CRUD operations, business logic, utility methods
 */
public class Main {

    private static final LoginService loginService = new LoginService();

    public static void main(String[] args) {
        try {
            // Initialize database
            initializeDatabase();

            // Main application loop
            runApplication();

        } catch (DatabaseConnectionException e) {
            ConsoleUtils.showError("Fatal database error: " + e.getMessage());
            System.out.println("  Please ensure sqlite3 is installed and accessible.");
            System.exit(1);
        }
    }


    /**
     * Initializes the database: creates tables and loads sample data if needed.
     */
    private static void initializeDatabase() throws DatabaseConnectionException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Check if database already exists
        if (db.testConnection() && db.tableExists("movie")) {
            ConsoleUtils.showInfo("Database loaded successfully.");
            return;
        }

        ConsoleUtils.showInfo("Initializing database...");

        // Execute schema
        String schemaPath = System.getProperty("user.dir") + "/db/schema.sql";
        if (Files.exists(Paths.get(schemaPath))) {
            db.executeScript(schemaPath);
            ConsoleUtils.showSuccess("Database schema created.");
        } else {
            ConsoleUtils.showError("schema.sql not found at: " + schemaPath);
            throw new DatabaseConnectionException("Schema file missing", "INIT");
        }

        // Load sample data
        String dataPath = System.getProperty("user.dir") + "/db/sample_data.sql";
        if (Files.exists(Paths.get(dataPath))) {
            db.executeScript(dataPath);
            ConsoleUtils.showSuccess("Sample data loaded.");
        }

        ConsoleUtils.showSuccess("Database initialization complete!");
    }

    /**
     * Main application loop - displays login menu and routes to role-specific menus.
     */
    private static void runApplication() {
        boolean running = true;

        while (running) {
            ConsoleUtils.showBanner();

            String[] options = {
                "Login as Admin",
                "Login as Customer",
                "Register New Customer",
                "Exit System"
            };
            int choice = ConsoleUtils.showMenu("MAIN MENU", options);

            try {
                switch (choice) {
                    case 1 -> loginAdmin();
                    case 2 -> loginCustomer();
                    case 3 -> registerCustomer();
                    case 4 -> {
                        running = false;
                        System.out.println();
                        System.out.println("  ╔═══════════════════════════════════════════╗");
                        System.out.println("  ║   Thank you for using the Cinema System! ║");
                        System.out.println("  ║   COMP 013 - Group 2 Final Project       ║");
                        System.out.println("  ╚═══════════════════════════════════════════╝");
                        System.out.println();
                    }
                }
            } catch (DatabaseConnectionException e) {
                ConsoleUtils.showError("Database error: " + e.getMessage());
            }
        }
    }


    /**
     * Handles admin login flow.
     */
    private static void loginAdmin() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("ADMIN LOGIN");
        String username = ConsoleUtils.getInput("Username");
        String password = ConsoleUtils.getInput("Password");

        try {
            Admin admin = loginService.loginAdmin(username, password);
            ConsoleUtils.showSuccess("Welcome, " + admin.getFullName() + "!");
            ConsoleUtils.pause();

            // Launch admin menu
            AdminMenu adminMenu = new AdminMenu(admin);
            adminMenu.show();

        } catch (InvalidLoginException e) {
            ConsoleUtils.showError(e.getMessage());
            ConsoleUtils.pause();
        }
    }

    /**
     * Handles customer login flow.
     */
    private static void loginCustomer() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("CUSTOMER LOGIN");
        String username = ConsoleUtils.getInput("Username");
        String password = ConsoleUtils.getInput("Password");

        try {
            Customer customer = loginService.loginCustomer(username, password);
            ConsoleUtils.showSuccess("Welcome back, " + customer.getFullName() + "!");
            ConsoleUtils.pause();

            // Launch customer menu
            CustomerMenu customerMenu = new CustomerMenu(customer);
            customerMenu.show();

        } catch (InvalidLoginException e) {
            ConsoleUtils.showError(e.getMessage());
            ConsoleUtils.pause();
        }
    }

    /**
     * Handles new customer registration.
     */
    private static void registerCustomer() throws DatabaseConnectionException {
        ConsoleUtils.showHeader("CUSTOMER REGISTRATION");
        String username = ConsoleUtils.getInput("Choose username");
        String password = ConsoleUtils.getInput("Choose password");
        String fullName = ConsoleUtils.getInput("Full Name");
        String email = ConsoleUtils.getInput("Email");
        String phone = ConsoleUtils.getInput("Phone Number");

        Customer newCustomer = loginService.registerCustomer(
                username, password, fullName, email, phone);

        if (newCustomer != null) {
            ConsoleUtils.showSuccess("Registration successful! Your ID: " + newCustomer.getUserId());
            ConsoleUtils.showInfo("You can now login with your credentials.");
        } else {
            ConsoleUtils.showError("Username already taken. Please choose another.");
        }
        ConsoleUtils.pause();
    }
}
