package com.example.bepnhataapp.features.recipes;

public class RecipeItem {
    private int imageResId;
    private String imageUrl;
    private byte[] imageData;
    private String name;
    private String calories;
    private String protein;
    private String time;
    private String category;

    public RecipeItem(int imageResId, String name, String calories, String protein, String time) {
        this.imageResId = imageResId;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.time = time;
        this.imageUrl = null;
        this.imageData = null;
    }

    public RecipeItem(int imageResId, String imageUrl, byte[] imageData, String name, String calories, String protein, String time, String category) {
        this.imageResId = imageResId;
        this.imageUrl = imageUrl;
        this.imageData = imageData;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.time = time;
        this.category = category;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getCategory() {
        return category;
    }
} 