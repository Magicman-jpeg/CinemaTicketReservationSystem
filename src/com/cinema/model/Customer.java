package com.cinema.model;

/**
 * Represents a Customer (from customer table in Excel).
 * Fields: customer_no, name, age, email_address, app_user, customer_username, customer_pass, mobile_no
 */
public class Customer extends User {

    private int customerNo;
    private String name;
    private int age;
    private String emailAddress;
    private String appUser;       // "Yes" or "No"
    private String mobileNo;

    public Customer() { super(); setRole("CUSTOMER"); }

    public Customer(int customerNo, String name, int age, String emailAddress,
                    String appUser, String username, String password, String mobileNo) {
        super(username, password, "CUSTOMER");
        this.customerNo = customerNo;
        this.name = name;
        this.age = age;
        this.emailAddress = emailAddress;
        this.appUser = appUser;
        this.mobileNo = mobileNo;
    }

    public int getCustomerNo() { return customerNo; }
    public void setCustomerNo(int customerNo) { this.customerNo = customerNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getAppUser() { return appUser; }
    public void setAppUser(String appUser) { this.appUser = appUser; }
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    /**
     * Determines discount eligibility based on age.
     */
    public String getDiscountType() {
        if (age >= 60) return "Senior Citizen";
        return "N/A";
    }

    /**
     * Whether the customer can login (is an app user with credentials).
     */
    public boolean canLogin() {
        return "Yes".equalsIgnoreCase(appUser) && getUsername() != null && !getUsername().isEmpty();
    }

    @Override
    public String displayInfo() {
        return String.format(
            "=== CUSTOMER PROFILE ===\n" +
            "Customer No:  %d\n" +
            "Name:         %s\n" +
            "Age:          %d\n" +
            "Email:        %s\n" +
            "Mobile:       %s\n" +
            "App User:     %s\n" +
            "Username:     %s\n" +
            "========================",
            customerNo, name, age, emailAddress, mobileNo, appUser,
            getUsername() != null ? getUsername() : "N/A");
    }

    @Override
    public String toString() {
        return String.format("| %-6d | %-25s | %-3d | %-30s | %-13s | %-3s |",
                customerNo, name, age, emailAddress, mobileNo, appUser);
    }

    public static String getTableHeader() {
        return String.format("| %-6s | %-25s | %-3s | %-30s | %-13s | %-3s |",
                "CustNo", "Name", "Age", "Email", "Mobile", "App");
    }

    public static String getTableDivider() {
        return "+--------+---------------------------+-----+--------------------------------+---------------+-----+";
    }
}
