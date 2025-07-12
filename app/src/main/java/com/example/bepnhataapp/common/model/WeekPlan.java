package com.example.bepnhataapp.common.model;

import java.time.LocalDate;
import java.util.List;

public class WeekPlan {
    public final LocalDate startDate;
    public final List<DayPlan> days;

    public WeekPlan(LocalDate startDate, List<DayPlan> days) {
        this.startDate = startDate;
        this.days = days;
    }
} 
