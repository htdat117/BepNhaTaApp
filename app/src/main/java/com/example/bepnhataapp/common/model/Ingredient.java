package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public long ingredientID;
    public String ingredientName;
    public String unit;
    public byte[] image; // BLOB
    public String imageLink; // URL text stored in DB
    public int imageResId = 0; // 0 means not using resource drawable
    public String quantity; // for UI display
    public String imageUrl; // for UI display (link ảnh)

    public Ingredient() {}

    public long getIngredientID() { return ingredientID; }
    public void setIngredientID(long ingredientID) { this.ingredientID = ingredientID; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    // Constructor cho UI: resource drawable
    public Ingredient(int imageResId, String name, String quantity) {
        this.imageResId = imageResId;
        this.ingredientName = name;
        this.quantity = quantity;
    }
    // Constructor cho UI: imageData
    public Ingredient(byte[] imageData, String name, String quantity) {
        this.image = imageData;
        this.ingredientName = name;
        this.quantity = quantity;
    }
    // Constructor cho UI: imageUrl
    public Ingredient(String imageUrl, String name, String quantity) {
        this.imageUrl = imageUrl;
        this.ingredientName = name;
        this.quantity = quantity;
    }
} 
