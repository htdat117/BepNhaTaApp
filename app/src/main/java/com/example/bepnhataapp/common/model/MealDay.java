package com.example.bepnhataapp.common.model;

public class MealDay {
    private long mealDayID;
    private long mealPlanID;
    private String date; // ISO yyyy-MM-dd
    private String note;

    public MealDay() {}

    public MealDay(long mealPlanID, String date, String note) {
        this.mealPlanID = mealPlanID;
        this.date = date;
        this.note = note;
    }

    public long getMealDayID() {
        return mealDayID;
    }

    public void setMealDayID(long mealDayID) {
        this.mealDayID = mealDayID;
    }

    public long getMealPlanID() {
        return mealPlanID;
    }

    public void setMealPlanID(long mealPlanID) {
        this.mealPlanID = mealPlanID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
} 
