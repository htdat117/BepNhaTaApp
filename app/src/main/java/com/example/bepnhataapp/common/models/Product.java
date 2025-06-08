package com.example.bepnhataapp.common.models;

public class Product {
    public int imageResId;
    public String name;
    public String kcal;
    public String nutrition;
    public String time;
    public String for2;
    public String for4;
    public String oldPrice;
    public String price;
    public float rating;
    public int reviewCount;
    public boolean isFavorite;

    public Product(int imageResId, String name, String kcal, String nutrition, String time, String for2, String for4, String oldPrice, String price, float rating, int reviewCount, boolean isFavorite) {
        this.imageResId = imageResId;
        this.name = name;
        this.kcal = kcal;
        this.nutrition = nutrition;
        this.time = time;
        this.for2 = for2;
        this.for4 = for4;
        this.oldPrice = oldPrice;
        this.price = price;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.isFavorite = isFavorite;
    }
}