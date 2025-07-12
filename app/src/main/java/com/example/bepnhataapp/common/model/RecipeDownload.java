package com.example.bepnhataapp.common.model;

public class RecipeDownload {
    private long customerID;
    private long recipeID;
    private String downloadedAt;

    public RecipeDownload() {}

    // getters & setters
    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }

    public String getDownloadedAt() { return downloadedAt; }
    public void setDownloadedAt(String downloadedAt) { this.downloadedAt = downloadedAt; }
} 
