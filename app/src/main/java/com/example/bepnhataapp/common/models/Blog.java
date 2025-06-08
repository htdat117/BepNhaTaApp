package com.example.bepnhataapp.common.models;

public class Blog {
    private String title;
    private String description;
    private String category;
    private int imageResId;
    private boolean isFavorite;

    public Blog(String title, String description, String category, int imageResId, boolean isFavorite) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getImageResId() { return imageResId; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
} 