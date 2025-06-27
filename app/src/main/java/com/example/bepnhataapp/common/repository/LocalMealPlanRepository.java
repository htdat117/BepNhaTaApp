package com.example.bepnhataapp.common.repository;

import android.content.Context;

import com.example.bepnhataapp.common.dao.MealPlanDao;
import com.example.bepnhataapp.common.model.WeekPlan;

import java.time.LocalDate;

/**
 * Basic on-device implementation backed by SQLite via MealPlanDao.
 * Currently only provides hasPlan() check; the rest returns null / no-op to keep
 * compile running until full implementation is provided later.
 */
public class LocalMealPlanRepository implements MealPlanRepository {

    private final MealPlanDao dao;

    public LocalMealPlanRepository(Context ctx) {
        this.dao = new MealPlanDao(ctx);
    }

    @Override
    public boolean hasPlan() {
        return !dao.getAll().isEmpty();
    }

    @Override
    public WeekPlan getWeekPlan() {
        // TODO: Build week plan from DB
        return null;
    }

    @Override
    public WeekPlan generateWeekPlan(LocalDate start) {
        // TODO: Generate real plan and persist
        return null;
    }

    @Override
    public WeekPlan generateEmptyWeekPlan(LocalDate start) {
        // TODO: Create empty plan structure
        return null;
    }

    @Override
    public void clear() {
        // TODO: Implement clearing logic
    }
} 