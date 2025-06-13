package com.example.bepnhataapp.common.models;

import java.io.Serializable;

public class Blog implements Serializable {
    private String title;
    private String description;
    private String category;
    private int imageResId;
    private boolean isFavorite;
    private String date;
    private int views;
    private int likes;

    public Blog(String title, String description, String category, int imageResId, boolean isFavorite) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
    }

    public Blog(String title, String description, String category, int imageResId, boolean isFavorite, String date, int views, int likes) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
        this.date = date;
        this.views = views;
        this.likes = likes;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getImageResId() { return imageResId; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public String getDate() { return date; }
    public int getViews() { return views; }
    public int getLikes() { return likes; }
} 