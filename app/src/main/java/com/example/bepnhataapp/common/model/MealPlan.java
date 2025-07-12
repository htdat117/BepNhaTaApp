package com.example.bepnhataapp.common.model;

public class MealPlan {
    private long mealPlanID;
    private Long customerID; // Nullable
    private String mealCategory;
    private String title;
    private String createdAt;
    private String imageThumb;
    private int totalDays;
    private double avgCalories;
    private double avgCarbs;
    private double avgProtein;
    private double avgFat;
    private String startDate;
    private String endDate;
    private String note;
    private String type;

    public MealPlan() {}

    // Getters and setters
    public long getMealPlanID() { return mealPlanID; }
    public void setMealPlanID(long mealPlanID) { this.mealPlanID = mealPlanID; }

    public Long getCustomerID() { return customerID; }
    public void setCustomerID(Long customerID) { this.customerID = customerID; }

    public String getMealCategory() { return mealCategory; }
    public void setMealCategory(String mealCategory) { this.mealCategory = mealCategory; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getImageThumb() { return imageThumb; }
    public void setImageThumb(String imageThumb) { this.imageThumb = imageThumb; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public double getAvgCalories() { return avgCalories; }
    public void setAvgCalories(double avgCalories) { this.avgCalories = avgCalories; }

    public double getAvgCarbs() { return avgCarbs; }
    public void setAvgCarbs(double avgCarbs) { this.avgCarbs = avgCarbs; }

    public double getAvgProtein() { return avgProtein; }
    public void setAvgProtein(double avgProtein) { this.avgProtein = avgProtein; }

    public double getAvgFat() { return avgFat; }
    public void setAvgFat(double avgFat) { this.avgFat = avgFat; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
} 
