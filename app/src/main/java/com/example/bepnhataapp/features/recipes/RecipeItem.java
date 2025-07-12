package com.example.bepnhataapp.features.recipes;

public class RecipeItem {
    private int imageResId;
    private String imageUrl;
    private byte[] imageData;
    private String name;
    private String calories;
    private String protein;
    private String time;
    private String category;

    // New fields for displaying benefit (nutrition tag) and difficulty level
    private String benefit;
    private String level;

    // Fields for like and comment counts
    private int likeCount;
    private int commentCount;

    public RecipeItem(int imageResId, String name, String calories, String protein, String time) {
        this.imageResId = imageResId;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.time = time;
        this.imageUrl = null;
        this.imageData = null;
    }

    public RecipeItem(int imageResId, String imageUrl, byte[] imageData, String name, String calories, String protein, String time, String category) {
        this.imageResId = imageResId;
        this.imageUrl = imageUrl;
        this.imageData = imageData;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.time = time;
        this.category = category;
    }

    // Getters
    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getProtein() {
        return protein;
    }

    public String getTime() {
        return time;
    }

    public String getBenefit() {
        return benefit;
    }

    public String getLevel() {
        // Fallback to the old protein field if level has not been set yet for backward compatibility
        return level != null ? level : protein;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getCategory() {
        return category;
    }

    public int getLikeCount() { return likeCount; }

    public int getCommentCount() { return commentCount; }

    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    // Setters for the new fields (optional usage)
    public void setBenefit(String benefit) { this.benefit = benefit; }

    public void setLevel(String level) { this.level = level; }

    public void setTime(String time) { this.time = time; }
} 
