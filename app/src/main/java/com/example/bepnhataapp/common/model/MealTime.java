package com.example.bepnhataapp.common.model;

public class MealTime {
    private long mealTimeID;
    private long mealDayID;
    private String mealType; // Sáng | Trưa | Tối
    private String note;

    public MealTime() {}

    public MealTime(long mealDayID, String mealType, String note) {
        this.mealDayID = mealDayID;
        this.mealType = mealType;
        this.note = note;
    }

    public long getMealTimeID() { return mealTimeID; }
    public void setMealTimeID(long mealTimeID) { this.mealTimeID = mealTimeID; }

    public long getMealDayID() { return mealDayID; }
    public void setMealDayID(long mealDayID) { this.mealDayID = mealDayID; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 
