package com.cinema.service;

import com.cinema.dao.*;
import com.cinema.exception.*;
import com.cinema.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * Service class for seat reservations.
 */
public class ReservationService {

    private final TransactionDAO transactionDAO;
    private final CinemaSeatDAO seatDAO;
    private final ScreeningDAO screeningDAO;

    public ReservationService() {
        this.transactionDAO = new TransactionDAO();
        this.seatDAO = new CinemaSeatDAO();
        this.screeningDAO = new ScreeningDAO();
    }

    /**
     * Reserves a seat for a customer.
     */
    public Transaction reserveSeat(int customerNo, String screeningId,
                                   String seatNo, String reservationType,
                                   String paymentMethod, String discountType,
                                   String adminId)
            throws InvalidSeatException, DuplicateReservationException,
                   PaymentFailedException, DatabaseConnectionException {

        // Validate seat exists
        CinemaSeat seat = seatDAO.findBySeatNo(seatNo);
        if (seat == null) {
            throw new InvalidSeatException("Seat " + seatNo + " does not exist.", seatNo);
        }

        // Check if already booked
        if (transactionDAO.isSeatBooked(screeningId, seatNo)) {
            throw new DuplicateReservationException(
                "Seat " + seatNo + " is already booked for this screening.",
                customerNo, 0, seatNo);
        }


        // Get screening for price
        Screening screening = screeningDAO.findById(screeningId);
        if (screening == null) {
            throw new InvalidSeatException("Screening not found.", seatNo);
        }

        double ticketPrice = screening.getTicketPrice();
        double bookingFee = "Online".equalsIgnoreCase(reservationType) ? 20.00 : 0.00;
        double discountAmount = 0.0;

        // Calculate discount
        if ("Senior Citizen".equalsIgnoreCase(discountType)) {
            discountAmount = ticketPrice * 0.20;
        } else if ("PWD".equalsIgnoreCase(discountType)) {
            discountAmount = ticketPrice * 0.20;
        }

        double totalPayment = ticketPrice + bookingFee - discountAmount;

        // Generate transaction ID: seatTypeId-M{movieId}-customerNo
        String txnId = String.format("%d-M%02d-%d",
                screening.getSeatTypeId(), screening.getMovieId(), customerNo);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        Transaction txn = new Transaction();
        txn.setTransactionId(txnId);
        txn.setTransactionDate(today);
        txn.setTransactionTime(now);
        txn.setCustomerNo(customerNo);
        txn.setSeatNo(seatNo);
        txn.setScreeningId(screeningId);
        txn.setMovieId(screening.getMovieId());
        txn.setSeatTypeId(screening.getSeatTypeId());
        txn.setReservationType(reservationType);
        txn.setAdminId(adminId);
        txn.setBookingFee(bookingFee);
        txn.setTicketPrice(ticketPrice);
        txn.setDiscountType(discountType != null ? discountType : "N/A");
        txn.setDiscountAmount(discountAmount);
        txn.setPaymentMethod(paymentMethod);
        txn.setTotalPayment(totalPayment);
        txn.setStatus(Transaction.STATUS_CONFIRMED);

        transactionDAO.insert(txn);
        return txn;
    }

    public boolean cancelReservation(String transactionId, int customerNo)
            throws DatabaseConnectionException {
        Transaction txn = transactionDAO.findById(transactionId);
        if (txn == null || txn.getCustomerNo() != customerNo) return false;
        if (!txn.isConfirmed()) return false;
        transactionDAO.cancel(transactionId);
        return true;
    }

    public List<Transaction> getCustomerReservations(int customerNo)
            throws DatabaseConnectionException {
        return transactionDAO.findByCustomerNo(customerNo);
    }

    /**
     * Generates ASCII seat map for a screening.
     */
    public String generateSeatMap(String screeningId) throws DatabaseConnectionException {
        Set<String> booked = seatDAO.getBookedSeats(screeningId);

        StringBuilder sb = new StringBuilder();
        sb.append("\n              ========== SCREEN ==========\n\n");
        sb.append("        1    2    3    4    5    6    7    8    9   10\n");
        sb.append("      +----+----+----+----+----+----+----+----+----+----+\n");

        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (String row : rows) {
            sb.append("  ").append(row).append("   |");
            for (int col = 1; col <= 10; col++) {
                String seatNo = row + col;
                if (booked.contains(seatNo)) {
                    sb.append(" XX |");
                } else {
                    sb.append(" -- |");
                }
            }
            sb.append("\n      +----+----+----+----+----+----+----+----+----+----+\n");
        }
        sb.append("\n  Legend: [ -- ] Available   [ XX ] Reserved\n");
        return sb.toString();
    }
}
