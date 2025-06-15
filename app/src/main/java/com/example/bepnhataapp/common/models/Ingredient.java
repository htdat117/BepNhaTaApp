package com.example.bepnhataapp.common.models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public int imageResId;
    public String name;
    public String quantity;

    public Ingredient(int imageResId, String name, String quantity) {
        this.imageResId = imageResId;
        this.name = name;
        this.quantity = quantity;
    }
} 