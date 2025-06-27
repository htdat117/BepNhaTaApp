package com.example.bepnhataapp.common.model;

public class Meal {
    public final int id;
    public final String title;
    public final int calories;
    public final int imageResId;

    public Meal(int id, String title, int calories, int imageResId) {
        this.id = id;
        this.title = title;
        this.calories = calories;
        this.imageResId = imageResId;
    }
} 