package com.example.bepnhataapp.common.repository;

import com.example.bepnhataapp.common.model.WeekPlan;
import java.time.LocalDate;

public interface MealPlanRepository {
    boolean hasPlan();
    WeekPlan getWeekPlan();
    WeekPlan generateWeekPlan(LocalDate start);
    WeekPlan generateEmptyWeekPlan(LocalDate start);
    void clear();
} 