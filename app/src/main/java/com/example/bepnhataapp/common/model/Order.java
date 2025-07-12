package com.example.bepnhataapp.common.model;

public class Order {
    private long orderID;
    private long cartID;
    private long addressID;
    private Long couponID; // nullable
    private String orderDate;
    private double totalPrice;
    private String status;
    private String paymentMethod;
    private String note;

    public Order() {}

    public Order(long orderID, long cartID, long addressID, Long couponID, String orderDate,
                 double totalPrice, String status, String paymentMethod, String note) {
        this.orderID = orderID;
        this.cartID = cartID;
        this.addressID = addressID;
        this.couponID = couponID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }

    public long getOrderID() { return orderID; }
    public void setOrderID(long orderID) { this.orderID = orderID; }

    public long getCartID() { return cartID; }
    public void setCartID(long cartID) { this.cartID = cartID; }

    public long getAddressID() { return addressID; }
    public void setAddressID(long addressID) { this.addressID = addressID; }

    public Long getCouponID() { return couponID; }
    public void setCouponID(Long couponID) { this.couponID = couponID; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 
