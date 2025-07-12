package com.example.bepnhataapp.features.blog;

public class BlogPostItem {
    private int imageResId;
    private String title;
    private String description;
    private String category;
    private String date;
    private int views;
    private int likes;

    public BlogPostItem(int imageResId, String title, String description, String category, String date, int views, int likes) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.views = views;
        this.likes = likes;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }
} 
