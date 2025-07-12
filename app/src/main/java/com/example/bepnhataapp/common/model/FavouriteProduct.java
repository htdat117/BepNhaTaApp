package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class FavouriteProduct implements Serializable {
    private long productID;
    private long customerID;
    private String createdAt;

    public FavouriteProduct() {}

    public FavouriteProduct(long productID, long customerID, String createdAt) {
        this.productID = productID;
        this.customerID = customerID;
        this.createdAt = createdAt;
    }

    public long getProductID() { return productID; }
    public void setProductID(long productID) { this.productID = productID; }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 
