package com.cinema.service;

import com.cinema.dao.*;
import com.cinema.exception.*;
import com.cinema.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class for handling seat reservations and ticket booking.
 * Contains business logic for the reservation workflow.
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
     * Reserves a seat for a customer at a specific screening.
     * Validates seat availability and creates the transaction.
     */
    public Transaction reserveSeat(int customerId, int screeningId,
                                   String seatRow, int seatColumn,
                                   String paymentMethod, double discount)
            throws InvalidSeatException, DuplicateReservationException,
                   PaymentFailedException, DatabaseConnectionException {

        // Validate row and column
        validateSeatPosition(seatRow, seatColumn);

        // Find the seat
        CinemaSeat seat = seatDAO.findByPosition(screeningId, seatRow, seatColumn);
        if (seat == null) {
            throw new InvalidSeatException(
                "Seat " + seatRow + seatColumn + " does not exist for this screening.",
                seatRow + seatColumn);
        }

        // Check availability
        if (!seat.isAvailable()) {
            throw new DuplicateReservationException(
                "Seat " + seat.getSeatLabel() + " is already reserved.",
                customerId, screeningId, seat.getSeatLabel());
        }


        // Get screening for price calculation
        Screening screening = screeningDAO.findById(screeningId);
        if (screening == null) {
            throw new InvalidSeatException("Screening not found.", seatRow + seatColumn);
        }

        // Calculate amount with discount
        double basePrice = screening.getTicketPrice();
        double finalAmount = basePrice * (1.0 - discount);

        // Validate payment
        if (finalAmount < 0) {
            throw new PaymentFailedException(
                "Invalid payment amount calculated.",
                paymentMethod, finalAmount);
        }

        // Create transaction
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Transaction txn = new Transaction();
        txn.setCustomerId(customerId);
        txn.setScreeningId(screeningId);
        txn.setSeatId(seat.getSeatId());
        txn.setSeatLabel(seat.getSeatLabel());
        txn.setTransactionDate(today);
        txn.setTransactionTime(now);
        txn.setAmountPaid(finalAmount);
        txn.setPaymentMethod(paymentMethod);
        txn.setStatus(Transaction.STATUS_CONFIRMED);

        // Reserve the seat
        seatDAO.reserveSeat(seat.getSeatId());

        // Save transaction
        int txnId = transactionDAO.insert(txn);
        txn.setTransactionId(txnId);

        return txn;
    }

    /**
     * Cancels a reservation and releases the seat.
     */
    public boolean cancelReservation(int transactionId, int customerId)
            throws DatabaseConnectionException {
        Transaction txn = transactionDAO.findById(transactionId);
        if (txn == null || txn.getCustomerId() != customerId) {
            return false;
        }
        if (!txn.isConfirmed()) {
            return false; // Already cancelled
        }

        // Release seat
        seatDAO.releaseSeat(txn.getSeatId());
        // Cancel transaction
        transactionDAO.cancel(transactionId);
        return true;
    }


    /**
     * Gets all reservations for a customer.
     */
    public List<Transaction> getCustomerReservations(int customerId)
            throws DatabaseConnectionException {
        return transactionDAO.findByCustomerId(customerId);
    }

    /**
     * Gets available seats for a screening.
     */
    public List<CinemaSeat> getAvailableSeats(int screeningId)
            throws DatabaseConnectionException {
        return seatDAO.findAvailableByScreeningId(screeningId);
    }

    /**
     * Gets all seats for a screening (for seat map display).
     */
    public List<CinemaSeat> getAllSeats(int screeningId)
            throws DatabaseConnectionException {
        return seatDAO.findByScreeningId(screeningId);
    }

    /**
     * Validates seat row and column are within valid range.
     */
    private void validateSeatPosition(String row, int column) throws InvalidSeatException {
        // Valid rows: A-K
        if (row == null || row.length() != 1 || row.charAt(0) < 'A' || row.charAt(0) > 'K') {
            throw new InvalidSeatException(
                "Invalid row '" + row + "'. Must be A through K.", row + column);
        }
        // Valid columns: 1-10
        if (column < 1 || column > 10) {
            throw new InvalidSeatException(
                "Invalid column '" + column + "'. Must be 1 through 10.", row + column);
        }
    }

    /**
     * Generates the ASCII seat map for a screening.
     */
    public String generateSeatMap(int screeningId) throws DatabaseConnectionException {
        List<CinemaSeat> seats = seatDAO.findByScreeningId(screeningId);
        if (seats.isEmpty()) {
            return "No seats found for this screening.";
        }

        // Build seat grid
        StringBuilder sb = new StringBuilder();
        sb.append("\n              ========== SCREEN ==========\n\n");
        sb.append("        1    2    3    4    5    6    7    8    9   10\n");
        sb.append("      +----+----+----+----+----+----+----+----+----+----+\n");

        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
        for (String row : rows) {
            sb.append("  ").append(row).append("   |");
            for (int col = 1; col <= 10; col++) {
                String status = getSeatStatus(seats, row, col);
                switch (status) {
                    case CinemaSeat.STATUS_AVAILABLE:
                        sb.append(" -- |"); // Available
                        break;
                    case CinemaSeat.STATUS_RESERVED:
                        sb.append(" XX |"); // Reserved
                        break;
                    case CinemaSeat.STATUS_OCCUPIED:
                        sb.append(" ## |"); // Occupied
                        break;
                    default:
                        sb.append(" ?? |"); // Unknown
                }
            }
            sb.append("\n      +----+----+----+----+----+----+----+----+----+----+\n");
        }
        sb.append("\n  Legend: [ -- ] Available   [ XX ] Reserved   [ ## ] Occupied\n");
        return sb.toString();
    }

    private String getSeatStatus(List<CinemaSeat> seats, String row, int col) {
        for (CinemaSeat seat : seats) {
            if (row.equals(seat.getSeatRow()) && col == seat.getSeatColumn()) {
                return seat.getStatus();
            }
        }
        return "UNKNOWN";
    }
}
