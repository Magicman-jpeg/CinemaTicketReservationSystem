package com.cinema.model;

/**
 * Represents a Transaction (from transaction table in Excel).
 * Fields: transaction_id, transaction_date, transaction_time, customer_no, seat_no,
 *         screening_id, movie_id, seat_type_id, reservation_type, admin_id,
 *         booking_fee, ticket_price, discount_type, discount_amount, payment_method,
 *         total_payment, status
 */
public class Transaction {

    private String transactionId;    // e.g. "2-M05-1001"
    private String transactionDate;  // YYYY-MM-DD
    private String transactionTime;  // HH:MM
    private int customerNo;
    private String seatNo;           // e.g. "A1"
    private String screeningId;      // e.g. "SUN-5"
    private int movieId;
    private int seatTypeId;
    private String reservationType;  // "Online" or "Onsite"
    private String adminId;          // null for Online, admin_id for Onsite
    private double bookingFee;       // 20.00 for Online, 0 for Onsite
    private double ticketPrice;      // from seat_type
    private String discountType;     // "N/A", "PWD", "Senior Citizen"
    private double discountAmount;
    private String paymentMethod;    // "E-Wallet", "Online Bank", "N/A"
    private double totalPayment;
    private String status;           // CONFIRMED, CANCELLED

    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    public Transaction() { this.status = STATUS_CONFIRMED; }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
    public String getTransactionTime() { return transactionTime; }
    public void setTransactionTime(String transactionTime) { this.transactionTime = transactionTime; }
    public int getCustomerNo() { return customerNo; }
    public void setCustomerNo(int customerNo) { this.customerNo = customerNo; }
    public String getSeatNo() { return seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }
    public String getScreeningId() { return screeningId; }
    public void setScreeningId(String screeningId) { this.screeningId = screeningId; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public int getSeatTypeId() { return seatTypeId; }
    public void setSeatTypeId(int seatTypeId) { this.seatTypeId = seatTypeId; }
    public String getReservationType() { return reservationType; }
    public void setReservationType(String reservationType) { this.reservationType = reservationType; }
    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    public double getBookingFee() { return bookingFee; }
    public void setBookingFee(double bookingFee) { this.bookingFee = bookingFee; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public double getTotalPayment() { return totalPayment; }
    public void setTotalPayment(double totalPayment) { this.totalPayment = totalPayment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isConfirmed() { return STATUS_CONFIRMED.equals(status); }
    public void cancel() { this.status = STATUS_CANCELLED; }

    @Override
    public String toString() {
        return String.format("| %-14s | %-10s | %-5s | %-6d | %-4s | %-7s | %-7s | PHP %7.2f | %-14s | %-9s |",
                transactionId, transactionDate, transactionTime, customerNo,
                seatNo != null ? seatNo : "N/A", screeningId,
                reservationType, totalPayment,
                discountType != null ? discountType : "N/A", status);
    }

    public static String getTableHeader() {
        return String.format("| %-14s | %-10s | %-5s | %-6s | %-4s | %-7s | %-7s | %-11s | %-14s | %-9s |",
                "Transaction ID", "Date", "Time", "CustNo", "Seat", "ScrID", "Type", "Total", "Discount", "Status");
    }

    public static String getTableDivider() {
        return "+----------------+------------+-------+--------+------+---------+---------+-------------+----------------+-----------+";
    }
}
