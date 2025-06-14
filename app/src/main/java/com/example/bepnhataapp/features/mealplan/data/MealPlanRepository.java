package com.example.bepnhataapp.features.mealplan.data;

import java.time.LocalDate;

public interface MealPlanRepository {
    boolean hasPlan();
    WeekPlan getWeekPlan();
    WeekPlan generateWeekPlan(LocalDate start);
    WeekPlan generateEmptyWeekPlan(LocalDate start);
    void clear();
} 