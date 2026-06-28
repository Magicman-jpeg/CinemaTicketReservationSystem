package com.cinema.service;

import com.cinema.dao.AdminDAO;
import com.cinema.dao.CustomerDAO;
import com.cinema.exception.DatabaseConnectionException;
import com.cinema.exception.InvalidLoginException;
import com.cinema.model.Admin;
import com.cinema.model.Customer;
import com.cinema.model.User;

/**
 * Service class for authentication.
 */
public class LoginService {

    private final AdminDAO adminDAO;
    private final CustomerDAO customerDAO;

    public LoginService() {
        this.adminDAO = new AdminDAO();
        this.customerDAO = new CustomerDAO();
    }

    public Admin loginAdmin(String username, String password)
            throws InvalidLoginException, DatabaseConnectionException {
        Admin admin = adminDAO.findByCredentials(username, password);
        if (admin == null) {
            throw new InvalidLoginException(
                "Invalid admin credentials.", username);
        }
        return admin;
    }

    public Customer loginCustomer(String username, String password)
            throws InvalidLoginException, DatabaseConnectionException {
        Customer customer = customerDAO.findByCredentials(username, password);
        if (customer == null) {
            throw new InvalidLoginException(
                "Invalid customer credentials.", username);
        }
        return customer;
    }

    public User login(String username, String password, String role)
            throws InvalidLoginException, DatabaseConnectionException {
        if ("ADMIN".equalsIgnoreCase(role)) return loginAdmin(username, password);
        else return loginCustomer(username, password);
    }

    public Customer registerCustomer(String username, String password,
                                     String name, int age, String email, String mobile)
            throws DatabaseConnectionException {
        if (customerDAO.usernameExists(username)) return null;
        Customer c = new Customer();
        c.setName(name);
        c.setAge(age);
        c.setEmailAddress(email);
        c.setAppUser("Yes");
        c.setUsername(username);
        c.setPassword(password);
        c.setMobileNo(mobile);
        int id = customerDAO.insert(c);
        c.setCustomerNo(id);
        return c;
    }
}
