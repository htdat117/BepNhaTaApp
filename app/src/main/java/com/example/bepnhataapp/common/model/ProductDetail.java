package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class ProductDetail implements Serializable {
    // Primary & foreign keys
    private long productID;   // also FK to PRODUCTS
    private Long recipeID;    // nullable FK to RECIPES (can be null if not a bundle/kit)

    // Nutrition information per serving size
    private double calo2;
    private double calo4;
    private double protein2;
    private double protein4;
    private double carbs2;
    private double carbs4;
    private double fat2;
    private double fat4;

    // Tags / categorisation
    private String foodTag;
    private String cuisine;
    private String nutritionTag; // NEW: keto, low-carb, gluten-free, ...

    // Cooking information per serving size
    private int cookingTimeMinutes2;
    private int cookingTimeMinutes4;

    // Storage & other details
    private String storageGuide;
    private String expiry;
    private String note;

    public ProductDetail() {
    }

    // -------------------- Getters & Setters -------------------- //
    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public double getCalo2() {
        return calo2;
    }

    public void setCalo2(double calo2) {
        this.calo2 = calo2;
    }

    public double getCalo4() {
        return calo4;
    }

    public void setCalo4(double calo4) {
        this.calo4 = calo4;
    }

    public double getProtein2() {
        return protein2;
    }

    public void setProtein2(double protein2) {
        this.protein2 = protein2;
    }

    public double getProtein4() {
        return protein4;
    }

    public void setProtein4(double protein4) {
        this.protein4 = protein4;
    }

    public double getCarbs2() {
        return carbs2;
    }

    public void setCarbs2(double carbs2) {
        this.carbs2 = carbs2;
    }

    public double getCarbs4() {
        return carbs4;
    }

    public void setCarbs4(double carbs4) {
        this.carbs4 = carbs4;
    }

    public double getFat2() {
        return fat2;
    }

    public void setFat2(double fat2) {
        this.fat2 = fat2;
    }

    public double getFat4() {
        return fat4;
    }

    public void setFat4(double fat4) {
        this.fat4 = fat4;
    }

    public String getFoodTag() {
        return foodTag;
    }

    public void setFoodTag(String foodTag) {
        this.foodTag = foodTag;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getNutritionTag() {
        return nutritionTag;
    }

    public void setNutritionTag(String nutritionTag) {
        this.nutritionTag = nutritionTag;
    }

    public int getCookingTimeMinutes2() {
        return cookingTimeMinutes2;
    }

    public void setCookingTimeMinutes2(int cookingTimeMinutes2) {
        this.cookingTimeMinutes2 = cookingTimeMinutes2;
    }

    public int getCookingTimeMinutes4() {
        return cookingTimeMinutes4;
    }

    public void setCookingTimeMinutes4(int cookingTimeMinutes4) {
        this.cookingTimeMinutes4 = cookingTimeMinutes4;
    }

    public String getStorageGuide() {
        return storageGuide;
    }

    public void setStorageGuide(String storageGuide) {
        this.storageGuide = storageGuide;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // ----------------- Backward compatibility (single-size) ----------------- //
    public double getCalo() { return calo2; }
    public void setCalo(double calo) { this.calo2 = calo; }

    public double getProtein() { return protein2; }
    public void setProtein(double protein) { this.protein2 = protein; }

    public double getCarbs() { return carbs2; }
    public void setCarbs(double carbs) { this.carbs2 = carbs; }

    public double getFat() { return fat2; }
    public void setFat(double fat) { this.fat2 = fat; }

    public int getCookingTimeMinutes() { return cookingTimeMinutes2; }
    public void setCookingTimeMinutes(int minutes) { this.cookingTimeMinutes2 = minutes; }
    // --------------------------------------------------------------------- //
} 
