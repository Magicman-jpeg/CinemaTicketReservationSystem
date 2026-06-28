package com.cinema.service;

import com.cinema.dao.AdminDAO;
import com.cinema.dao.CustomerDAO;
import com.cinema.exception.DatabaseConnectionException;
import com.cinema.exception.InvalidLoginException;
import com.cinema.model.Admin;
import com.cinema.model.Customer;
import com.cinema.model.User;

/**
 * Service class for handling user authentication.
 * Validates credentials against the database and returns the appropriate User type.
 * Demonstrates polymorphism - returns User reference that can be Admin or Customer.
 */
public class LoginService {

    private final AdminDAO adminDAO;
    private final CustomerDAO customerDAO;

    public LoginService() {
        this.adminDAO = new AdminDAO();
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Attempts admin login with provided credentials.
     * @throws InvalidLoginException if credentials are invalid
     */
    public Admin loginAdmin(String username, String password)
            throws InvalidLoginException, DatabaseConnectionException {
        Admin admin = adminDAO.findByCredentials(username, password);
        if (admin == null) {
            throw new InvalidLoginException(
                "Invalid admin credentials. Please check your username and password.",
                username);
        }
        return admin;
    }


    /**
     * Attempts customer login with provided credentials.
     * @throws InvalidLoginException if credentials are invalid
     */
    public Customer loginCustomer(String username, String password)
            throws InvalidLoginException, DatabaseConnectionException {
        Customer customer = customerDAO.findByCredentials(username, password);
        if (customer == null) {
            throw new InvalidLoginException(
                "Invalid customer credentials. Please check your username and password.",
                username);
        }
        return customer;
    }

    /**
     * Generic login that tries both admin and customer (polymorphic return).
     */
    public User login(String username, String password, String role)
            throws InvalidLoginException, DatabaseConnectionException {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return loginAdmin(username, password);
        } else if ("CUSTOMER".equalsIgnoreCase(role)) {
            return loginCustomer(username, password);
        } else {
            throw new InvalidLoginException("Invalid role specified: " + role, username);
        }
    }

    /**
     * Registers a new customer account.
     */
    public Customer registerCustomer(String username, String password, String fullName,
                                     String email, String phoneNumber)
            throws DatabaseConnectionException {
        // Check if username exists
        if (customerDAO.usernameExists(username)) {
            return null; // Username taken
        }

        Customer customer = new Customer(username, password, fullName, email,
                phoneNumber, "REGULAR", java.time.LocalDate.now().toString());
        int id = customerDAO.insert(customer);
        customer.setUserId(id);
        return customer;
    }
}
