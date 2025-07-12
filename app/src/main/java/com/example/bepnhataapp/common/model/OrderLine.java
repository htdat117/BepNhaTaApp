package com.example.bepnhataapp.common.model;

public class OrderLine {
    private long orderLineID;
    private long orderID;
    private long productID;
    private int quantity;
    private double totalPrice;

    public OrderLine() {}

    public OrderLine(long orderLineID, long orderID, long productID, int quantity, double totalPrice) {
        this.orderLineID = orderLineID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public long getOrderLineID() { return orderLineID; }
    public void setOrderLineID(long orderLineID) { this.orderLineID = orderLineID; }

    public long getOrderID() { return orderID; }
    public void setOrderID(long orderID) { this.orderID = orderID; }

    public long getProductID() { return productID; }
    public void setProductID(long productID) { this.productID = productID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
} 
