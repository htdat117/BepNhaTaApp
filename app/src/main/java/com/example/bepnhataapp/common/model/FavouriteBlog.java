package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class FavouriteBlog implements Serializable {
    private long blogID;
    private long customerID;
    private String createdAt;

    public FavouriteBlog() {}

    public FavouriteBlog(long blogID, long customerID, String createdAt) {
        this.blogID = blogID;
        this.customerID = customerID;
        this.createdAt = createdAt;
    }

    public long getBlogID() { return blogID; }
    public void setBlogID(long blogID) { this.blogID = blogID; }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 
