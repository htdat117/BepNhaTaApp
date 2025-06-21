package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class Product implements Serializable {
    private long productID;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int salePercent;
    private byte[] productThumb; // may be null
    private int commentAmount;
    private String category;
    private int inventory;
    private int soldQuantity;
    private double avgRating;
    private String status;
    private String createdDate;
    private String updatedDate;

    public Product() {}

    // getters & setters...
    public long getProductID() { return productID; }
    public void setProductID(long productID) { this.productID = productID; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    public int getProductPrice() { return productPrice; }
    public void setProductPrice(int productPrice) { this.productPrice = productPrice; }
    public int getSalePercent() { return salePercent; }
    public void setSalePercent(int salePercent) { this.salePercent = salePercent; }
    public byte[] getProductThumb() { return productThumb; }
    public void setProductThumb(byte[] productThumb) { this.productThumb = productThumb; }
    public int getCommentAmount() { return commentAmount; }
    public void setCommentAmount(int commentAmount) { this.commentAmount = commentAmount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getInventory() { return inventory; }
    public void setInventory(int inventory) { this.inventory = inventory; }
    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }
    public double getAvgRating() { return avgRating; }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public String getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }
} 