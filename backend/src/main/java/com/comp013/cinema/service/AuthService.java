package com.comp013.cinema.service;

import com.comp013.cinema.dao.CinemaDao;
import com.comp013.cinema.exception.InvalidLoginException;
import com.comp013.cinema.model.Admin;
import com.comp013.cinema.model.Customer;
import com.comp013.cinema.model.User;
import com.comp013.cinema.util.InputValidator;

public class AuthService {
    private final CinemaDao cinemaDao;

    public AuthService(CinemaDao cinemaDao) {
        this.cinemaDao = cinemaDao;
    }

    public User login(String username, String password) throws InvalidLoginException {
        try {
            InputValidator.requireNonEmpty(username, "Username");
            InputValidator.requireNonEmpty(password, "Password");
            Admin admin = cinemaDao.findAdmin(username, password);
            if (admin != null) {
                return admin;
            }
            Customer customer = cinemaDao.findCustomer(username, password);
            if (customer != null) {
                return customer;
            }
        } catch (Exception e) {
            if (e instanceof InvalidLoginException) {
                throw (InvalidLoginException) e;
            }
        }
        throw new InvalidLoginException("Invalid username/password.");
    }
}
