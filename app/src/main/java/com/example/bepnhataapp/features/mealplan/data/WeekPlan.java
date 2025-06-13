package com.example.bepnhataapp.features.mealplan.data;

import java.time.LocalDate;
import java.util.List;

public class WeekPlan {
    public final LocalDate start;
    public final List<DayPlan> days;

    public WeekPlan(LocalDate start, List<DayPlan> days) {
        this.start = start;
        this.days = days;
    }
} 