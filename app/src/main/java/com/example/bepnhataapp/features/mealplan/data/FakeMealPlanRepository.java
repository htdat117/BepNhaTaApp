package com.example.bepnhataapp.features.mealplan.data;

import android.util.Pair;
import com.example.bepnhataapp.R;
import java.time.LocalDate;
import java.util.*;

public class FakeMealPlanRepository implements MealPlanRepository {

    private WeekPlan cached;

    @Override
    public boolean hasPlan() {
        return cached != null;
    }

    @Override
    public WeekPlan getWeekPlan() {
        return cached;
    }

    @Override
    public WeekPlan generateWeekPlan(LocalDate start) {
        Meal meal = new Meal(1, "Sườn nướng mật ong", 1560, R.drawable.placeholder_banner_background);
        List<Pair<MealTime, Meal>> mealsForDay = Arrays.asList(
                new Pair<>(MealTime.BREAKFAST, meal),
                new Pair<>(MealTime.LUNCH, meal),
                new Pair<>(MealTime.DINNER, meal),
                new Pair<>(MealTime.SNACK, meal));

        List<DayPlan> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(new DayPlan(start.plusDays(i), mealsForDay));
        }
        cached = new WeekPlan(start, days);
        return cached;
    }

    @Override
    public void clear() {
        cached = null;
    }
} 