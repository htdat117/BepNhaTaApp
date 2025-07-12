package com.example.bepnhataapp.common.model;

public class FavouriteRecipe {
    private long recipeID;
    private long customerID;
    private String createdAt;

    public FavouriteRecipe() {}

    // getters & setters
    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 
