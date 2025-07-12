package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class FoodCalo implements Serializable {
    private long foodCaloID;
    private String foodName;
    private String foodThumb;
    private int caloPerOneHundredGrams;

    public long getFoodCaloID() { return foodCaloID; }
    public void setFoodCaloID(long foodCaloID) { this.foodCaloID = foodCaloID; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getFoodThumb() { return foodThumb; }
    public void setFoodThumb(String foodThumb) { this.foodThumb = foodThumb; }
    public int getCaloPerOneHundredGrams() { return caloPerOneHundredGrams; }
    public void setCaloPerOneHundredGrams(int caloPerOneHundredGrams) { this.caloPerOneHundredGrams = caloPerOneHundredGrams; }
} 
