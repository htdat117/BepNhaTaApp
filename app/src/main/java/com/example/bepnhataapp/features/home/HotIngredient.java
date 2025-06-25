package com.example.bepnhataapp.features.home;

public class HotIngredient {
    private String imageUrl;
    private String title;
    private String price;

    public HotIngredient(String imageUrl, String title, String price) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }
} 