package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class ProductDetail implements Serializable {
    private long productID;
    private Long recipeID; // nullable
    private double calo;
    private double protein;
    private double carbs;
    private double fat;
    private String foodTag;
    private String cuisine;
    private int cookingTimeMinutes;
    private String storageGuide;
    private String expiry;
    private String note;

    public ProductDetail() {}

    // Getters & setters
    public long getProductID() { return productID; }
    public void setProductID(long productID) { this.productID = productID; }
    public Long getRecipeID() { return recipeID; }
    public void setRecipeID(Long recipeID) { this.recipeID = recipeID; }
    public double getCalo() { return calo; }
    public void setCalo(double calo) { this.calo = calo; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public String getFoodTag() { return foodTag; }
    public void setFoodTag(String foodTag) { this.foodTag = foodTag; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public int getCookingTimeMinutes() { return cookingTimeMinutes; }
    public void setCookingTimeMinutes(int cookingTimeMinutes) { this.cookingTimeMinutes = cookingTimeMinutes; }
    public String getStorageGuide() { return storageGuide; }
    public void setStorageGuide(String storageGuide) { this.storageGuide = storageGuide; }
    public String getExpiry() { return expiry; }
    public void setExpiry(String expiry) { this.expiry = expiry; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 