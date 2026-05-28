package com.comp013.cinema.model;

public class Customer extends User {
    private final int customerNo;

    public Customer(int customerNo, String username, String password, String name) {
        super(username, password, name);
        this.customerNo = customerNo;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }
}
