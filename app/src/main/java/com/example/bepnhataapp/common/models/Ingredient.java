package com.example.bepnhataapp.common.models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public int imageResId = 0; // 0 means not using resource drawable
    public byte[] imageData; // can be null
    public String imageUrl; // can be null
    public String name;
    public String quantity;

    public Ingredient(int imageResId, String name, String quantity) {
        this.imageResId = imageResId;
        this.name = name;
        this.quantity = quantity;
    }

    public Ingredient(byte[] imageData, String name, String quantity) {
        this.imageData = imageData;
        this.name = name;
        this.quantity = quantity;
    }

    public Ingredient(String imageUrl, String name, String quantity){
        this.imageUrl = imageUrl;
        this.name = name;
        this.quantity = quantity;
    }
} 