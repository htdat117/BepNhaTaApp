package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class Blog implements Serializable {
    private long blogID;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private boolean isFavorite;
    private String date;
    private int likes;
    private int views;

    public Blog(long blogID, String title, String description, String category, String imageUrl, boolean isFavorite, String date, int likes, int views) {
        this.blogID = blogID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
        this.date = date;
        this.likes = likes;
        this.views = views;
    }

    // Backward-compatibility constructor (default blogID = 0)
    public Blog(String title, String description, String category, String imageUrl, boolean isFavorite, String date, int likes, int views) {
        this(0, title, description, category, imageUrl, isFavorite, date, likes, views);
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

    public long getBlogID() { return blogID; }
    public void setBlogID(long id) { this.blogID = id; }
} 
