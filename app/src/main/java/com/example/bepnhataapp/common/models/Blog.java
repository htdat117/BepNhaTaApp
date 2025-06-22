package com.example.bepnhataapp.common.models;

import java.io.Serializable;

public class Blog implements Serializable {
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private boolean isFavorite;
    private String date;
    private int likes;
    private int views;

    public Blog(String title, String description, String category, String imageUrl, boolean isFavorite, String date, int likes, int views) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
        this.date = date;
        this.likes = likes;
        this.views = views;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFavorite() { return isFavorite; }
    public String getDate() { return date; }
    public int getLikes() { return likes; }
    public int getViews() { return views; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }
} 