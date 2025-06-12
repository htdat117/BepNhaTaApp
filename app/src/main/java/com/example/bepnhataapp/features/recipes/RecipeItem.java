package com.example.bepnhataapp.features.recipes;

public class RecipeItem {
    private int imageResId;
    private String name;
    private String calories;
    private String protein;
    private String time;

    public RecipeItem(int imageResId, String name, String calories, String protein, String time) {
        this.imageResId = imageResId;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.time = time;
    }

    // Getters
    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getProtein() {
        return protein;
    }

    public String getTime() {
        return time;
    }
} 