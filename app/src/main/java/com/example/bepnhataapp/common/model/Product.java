package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class Product implements Serializable {
    // Primary key
    private long productID;

    // Basic info
    private String productName;
    private String productDescription;

    // Pricing for 2-person and 4-person serving sizes
    private int productPrice2;
    private int productPrice4;

    // Sale percent for 2-person and 4-person packages
    private int salePercent2;
    private int salePercent4;

    // Thumbnail image (URL or file path)
    private String productThumb;

    // Category & inventory
    private String category;
    private int inventory2;   // stock for 2-person size
    private int inventory4;   // stock for 4-person size

    // Social metrics
    private double avgRating;
    private int commentAmount;

    public Product() {
    }

    // -------------------- Getters & Setters -------------------- //
    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductPrice2() {
        return productPrice2;
    }

    public void setProductPrice2(int productPrice2) {
        this.productPrice2 = productPrice2;
    }

    public int getProductPrice4() {
        return productPrice4;
    }

    public void setProductPrice4(int productPrice4) {
        this.productPrice4 = productPrice4;
    }

    public int getSalePercent2() {
        return salePercent2;
    }

    public void setSalePercent2(int salePercent2) {
        this.salePercent2 = salePercent2;
    }

    public int getSalePercent4() {
        return salePercent4;
    }

    public void setSalePercent4(int salePercent4) {
        this.salePercent4 = salePercent4;
    }

    public String getProductThumb() {
        return productThumb;
    }

    public void setProductThumb(String productThumb) {
        this.productThumb = productThumb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getInventory2() {
        return inventory2;
    }

    public void setInventory2(int inventory2) {
        this.inventory2 = inventory2;
    }

    public int getInventory4() {
        return inventory4;
    }

    public void setInventory4(int inventory4) {
        this.inventory4 = inventory4;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(int commentAmount) {
        this.commentAmount = commentAmount;
    }

    // ----------------- Backward compatibility (single-size fields) ----------------- //
    /**
     * Legacy getter: returns price of 2-person kit for backward compatibility.
     */
    public int getProductPrice() {
        return productPrice2;
    }

    /**
     * Legacy setter: sets price for 2-person kit.
     */
    public void setProductPrice(int price) {
        this.productPrice2 = price;
    }

    public int getSalePercent() {
        return salePercent2;
    }

    public void setSalePercent(int salePercent) {
        this.salePercent2 = salePercent;
    }

    public int getInventory() {
        return inventory2;
    }

    public void setInventory(int inventory) {
        this.inventory2 = inventory;
    }
    // ----------------------------------------------------------------------------- //
} 
