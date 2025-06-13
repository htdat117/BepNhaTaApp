package com.example.bepnhataapp.features.mealplan.data;

import android.util.Pair;
import java.time.LocalDate;
import java.util.List;

public class DayPlan {
    public final LocalDate date;
    public final List<Pair<MealTime, Meal>> meals;

    public DayPlan(LocalDate date, List<Pair<MealTime, Meal>> meals) {
        this.date = date;
        this.meals = meals;
    }
} 