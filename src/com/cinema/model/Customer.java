package com.cinema.model;

/**
 * Represents a Customer user in the system.
 * Inherits from User (demonstrates inheritance).
 * Customers can browse movies, reserve seats, and view their bookings.
 */
public class Customer extends User {

    private String phoneNumber;
    private String membershipType; // REGULAR, PREMIUM, VIP
    private String registrationDate;

    public static final String ROLE = "CUSTOMER";

    // Default constructor
    public Customer() {
        super();
        setRole(ROLE);
    }

    // Parameterized constructor
    public Customer(int userId, String username, String password,
                    String fullName, String email, String phoneNumber,
                    String membershipType, String registrationDate) {
        super(userId, username, password, fullName, email, ROLE);
        this.phoneNumber = phoneNumber;
        this.membershipType = membershipType;
        this.registrationDate = registrationDate;
    }

    // Constructor without ID
    public Customer(String username, String password, String fullName,
                    String email, String phoneNumber, String membershipType,
                    String registrationDate) {
        super(username, password, fullName, email, ROLE);
        this.phoneNumber = phoneNumber;
        this.membershipType = membershipType;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Calculates discount based on membership type (polymorphism in action).
     */
    public double getDiscount() {
        switch (membershipType) {
            case "VIP":
                return 0.20; // 20% discount
            case "PREMIUM":
                return 0.10; // 10% discount
            case "REGULAR":
            default:
                return 0.0;  // No discount
        }
    }

    /**
     * Polymorphic implementation of displayInfo.
     */
    @Override
    public String displayInfo() {
        return String.format(
            "=== CUSTOMER PROFILE ===\n" +
            "ID:           %d\n" +
            "Username:     %s\n" +
            "Full Name:    %s\n" +
            "Email:        %s\n" +
            "Phone:        %s\n" +
            "Membership:   %s\n" +
            "Registered:   %s\n" +
            "Discount:     %.0f%%\n" +
            "========================",
            getUserId(), getUsername(), getFullName(),
            getEmail(), phoneNumber, membershipType,
            registrationDate, getDiscount() * 100
        );
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-15s | %-20s | %-25s | %-13s | %-9s | %-10s |",
                getUserId(), getUsername(), getFullName(), getEmail(),
                phoneNumber, membershipType, registrationDate);
    }

    public static String getTableHeader() {
        return String.format("| %-4s | %-15s | %-20s | %-25s | %-13s | %-9s | %-10s |",
                "ID", "Username", "Full Name", "Email", "Phone", "Member", "Registered");
    }

    public static String getTableDivider() {
        return "+------+-----------------+----------------------+---------------------------+---------------+-----------+------------+";
    }
}
