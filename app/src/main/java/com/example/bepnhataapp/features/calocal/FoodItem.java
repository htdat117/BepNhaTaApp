package com.example.bepnhataapp.features.calocal;

public class FoodItem {
    private String name;
    private double weight;
    private double calories;
    private int imageResId;

    public FoodItem(String name, double weight, double calories, int imageResId) {
        this.name = name;
        this.weight = weight;
        this.calories = calories;
        this.imageResId = imageResId;
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

    public int getImageResId() {
        return imageResId;
    }
} 