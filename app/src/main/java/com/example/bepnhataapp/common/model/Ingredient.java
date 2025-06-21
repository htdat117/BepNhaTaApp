package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private long ingredientID;
    private String ingredientName;
    private String unit;
    private byte[] image; // BLOB

    public Ingredient() {}

    public long getIngredientID() { return ingredientID; }
    public void setIngredientID(long ingredientID) { this.ingredientID = ingredientID; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
} 