package com.example.bepnhataapp.common.model;

public class RecipeDetail {
    private long recipeID;
    private double calo;
    private double protein;
    private double carbs;
    private double fat;
    private String foodTag;
    private String cuisine;
    private int cookingTimeMinutes;
    private String flavor;
    private String benefit;
    private String level;

    public RecipeDetail() {}

    public RecipeDetail(long recipeID, double calo, double protein, double carbs, double fat, String foodTag, String cuisine, int cookingTimeMinutes, String flavor, String benefit, String level) {
        this.recipeID = recipeID;
        this.calo = calo;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.foodTag = foodTag;
        this.cuisine = cuisine;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.flavor = flavor;
        this.benefit = benefit;
        this.level = level;
    }

    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }

    public double getCalo() { return calo; }
    public void setCalo(double calo) { this.calo = calo; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }

    public String getFoodTag() { return foodTag; }
    public void setFoodTag(String foodTag) { this.foodTag = foodTag; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public int getCookingTimeMinutes() { return cookingTimeMinutes; }
    public void setCookingTimeMinutes(int cookingTimeMinutes) { this.cookingTimeMinutes = cookingTimeMinutes; }

    public String getFlavor() { return flavor; }
    public void setFlavor(String flavor) { this.flavor = flavor; }

    public String getBenefit() { return benefit; }
    public void setBenefit(String benefit) { this.benefit = benefit; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
} 
