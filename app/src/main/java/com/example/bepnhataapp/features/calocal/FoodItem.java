package com.example.bepnhataapp.features.calocal;

public class FoodItem {
    private String name;
    private double weight;
    private double calories;
    private String imageUrl; // link ảnh từ database
    private int imageResId;  // fallback nếu không có url

    // Constructor cho URL
    public FoodItem(String name, double weight, double calories, String imageUrl) {
        this.name = name;
        this.weight = weight;
        this.calories = calories;
        this.imageUrl = imageUrl;
        this.imageResId = 0;
    }

    // Constructor cho resource nội bộ
    public FoodItem(String name, double weight, double calories, int imageResId) {
        this.name = name;
        this.weight = weight;
        this.calories = calories;
        this.imageResId = imageResId;
        this.imageUrl = null;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getCalories() {
        return calories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageResId() {
        return imageResId;
    }
} 
