package com.example.bepnhataapp.common.model;

public class MealRecipe {
    private long mealTimeID;
    private long recipeID;

    public MealRecipe() {}

    public MealRecipe(long mealTimeID, long recipeID) {
        this.mealTimeID = mealTimeID;
        this.recipeID = recipeID;
    }

    public long getMealTimeID() {
        return mealTimeID;
    }

    public void setMealTimeID(long mealTimeID) {
        this.mealTimeID = mealTimeID;
    }

    public long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(long recipeID) {
        this.recipeID = recipeID;
    }
} 
