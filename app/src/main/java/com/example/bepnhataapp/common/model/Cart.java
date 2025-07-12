package com.example.bepnhataapp.common.model;

public class Cart {
    private long cartID;
    private long customerID;
    private String createdAt;

    public Cart() {}

    public Cart(long cartID, long customerID, String createdAt) {
        this.cartID = cartID;
        this.customerID = customerID;
        this.createdAt = createdAt;
    }

    public long getCartID() { return cartID; }
    public void setCartID(long cartID) { this.cartID = cartID; }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 
