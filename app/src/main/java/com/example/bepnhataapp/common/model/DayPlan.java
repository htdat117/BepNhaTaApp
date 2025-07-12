package com.example.bepnhataapp.common.model;

import android.util.Pair;
import java.time.LocalDate;
import java.util.List;

public class DayPlan {
    public final LocalDate date;
    public final List<Pair<MealTimeEnum, Meal>> meals;

    public DayPlan(LocalDate date, List<Pair<MealTimeEnum, Meal>> meals) {
        this.date = date;
        this.meals = meals;
    }

    // Simple enum representing time slots within a day
    public enum MealTimeEnum {
        BREAKFAST, LUNCH, DINNER, SNACK
    }
} 
