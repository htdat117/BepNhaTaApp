package com.example.bepnhataapp.common.model;

public class DownloadedRecipe {
    private String title;
    private String cal;
    private String type;
    private String time;
    private int imageResId;
    private String imageThumb;
    private int recipeId;
    private String benefit;
    private String level;

    public DownloadedRecipe(int recipeId, String title, String cal, String type, String time, int imageResId, String imageThumb, String benefit, String level) {
        this.recipeId = recipeId;
        this.title = title;
        this.cal = cal;
        this.type = type;
        this.time = time;
        this.imageResId = imageResId;
        this.imageThumb = imageThumb;
        this.benefit = benefit;
        this.level = level;
    }

    public String getTitle() { return title; }
    public String getCal() { return cal; }
    public String getType() { return type; }
    public String getTime() { return time; }
    public int getImageResId() { return imageResId; }
    public String getImageThumb() { return imageThumb; }
    public int getRecipeId() { return recipeId; }
    public String getBenefit() { return benefit; }
    public String getLevel() { return level; }
} 
