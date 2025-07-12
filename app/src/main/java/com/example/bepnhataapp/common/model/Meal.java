package com.example.bepnhataapp.common.model;

public class Meal {
    public final int id;
    public final String title;
    public final int calories;
    public final int imageResId;
    public final String imageUrl;
    public final double carbs;
    public final double protein;
    public final double fat;

    public Meal(int id, String title, int calories, int imageResId) {
        this(id, title, calories, imageResId, null, 0, 0, 0);
    }

    public Meal(int id, String title, int calories, String imageUrl) {
        this(id, title, calories, 0, imageUrl, 0, 0, 0);
    }

    public Meal(int id, String title, int calories, int imageResId, String url, double carbs, double protein, double fat) {
        this.id = id;
        this.title = title;
        this.calories = calories;
        this.imageResId = imageResId;
        this.imageUrl = url;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
} 
