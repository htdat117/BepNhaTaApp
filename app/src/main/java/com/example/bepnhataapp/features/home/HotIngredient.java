package com.example.bepnhataapp.features.home;

public class HotIngredient {
    private int imageResId;
    private String title;
    private String price;

    public HotIngredient(int imageResId, String title, String price) {
        this.imageResId = imageResId;
        this.title = title;
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }
} 