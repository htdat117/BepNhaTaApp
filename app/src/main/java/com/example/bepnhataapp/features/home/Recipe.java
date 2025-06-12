package com.example.bepnhataapp.features.home;

public class Recipe {
    private int imageResId;
    private String title;
    private String calories;
    private String time;

    public Recipe(int imageResId, String title, String calories, String time) {
        this.imageResId = imageResId;
        this.title = title;
        this.calories = calories;
        this.time = time;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getCalories() {
        return calories;
    }

    public String getTime() {
        return time;
    }
} 