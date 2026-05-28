package com.comp013.cinema.service;

import com.comp013.cinema.dao.CinemaDao;
import com.comp013.cinema.exception.DuplicateReservationException;
import com.comp013.cinema.exception.InvalidSeatException;
import com.comp013.cinema.exception.PaymentFailedException;
import com.comp013.cinema.model.Customer;
import com.comp013.cinema.util.InputValidator;

import java.util.UUID;

public class ReservationService {
    private final CinemaDao cinemaDao;

    public ReservationService(CinemaDao cinemaDao) {
        this.cinemaDao = cinemaDao;
    }

    public void reserveSeat(Customer customer, String seatNo, String screeningId, int movieId, double ticketPrice,
                            String discountType, String paymentMethod)
            throws InvalidSeatException, DuplicateReservationException, PaymentFailedException {
        try {
            InputValidator.validateSeatNo(seatNo);
            if (!cinemaDao.seatExists(seatNo)) {
                throw new InvalidSeatException("Seat does not exist in cinema_seat table.");
            }
            if (cinemaDao.seatBookedForScreening(seatNo, screeningId)) {
                throw new DuplicateReservationException("Seat is already reserved for this screening.");
            }
            double discountAmount = "Senior Citizen".equalsIgnoreCase(discountType) ? (ticketPrice + 20) * 0.20 : 0.0;
            double totalPayment = ticketPrice + 20 - discountAmount;
            if (totalPayment <= 0) {
                throw new PaymentFailedException("Payment calculation failed.");
            }
            String txId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            cinemaDao.insertTransaction(txId, customer.getCustomerNo(), seatNo, screeningId, movieId, ticketPrice,
                    discountAmount, totalPayment, paymentMethod, discountType);
        } catch (InvalidSeatException | DuplicateReservationException | PaymentFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentFailedException("Reservation failed: " + e.getMessage());
        }
    }
}
