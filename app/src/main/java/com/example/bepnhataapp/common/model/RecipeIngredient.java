package com.example.bepnhataapp.common.model;

public class RecipeIngredient {
    private long ingredientID;
    private long recipeID;
    private double quantity;
    private String nameIngredient;

    public RecipeIngredient() {}

    // getters & setters
    public long getIngredientID() { return ingredientID; }
    public void setIngredientID(long ingredientID) { this.ingredientID = ingredientID; }

    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getNameIngredient() { return nameIngredient; }
    public void setNameIngredient(String nameIngredient) { this.nameIngredient = nameIngredient; }
}
