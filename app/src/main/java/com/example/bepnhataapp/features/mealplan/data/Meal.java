package com.example.bepnhataapp.features.mealplan.data;

public class Meal {
    public final int id;
    public final String name;
    public final int kcal;
    public final int imageRes;

    public Meal(int id, String name, int kcal, int imageRes) {
        this.id = id;
        this.name = name;
        this.kcal = kcal;
        this.imageRes = imageRes;
    }
} 