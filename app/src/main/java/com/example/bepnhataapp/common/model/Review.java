package com.example.bepnhataapp.common.model;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {
    public int avatarResId;
    public String avatarUrl;
    public String userName;
    public float rating;
    public String timeAgo;
    public String comment;
    public List<Integer> imageResIds;
    public java.util.List<String> imageUrls;

    public Review(int avatarResId, String userName, float rating, String timeAgo, String comment, java.util.List<Integer> imageResIds) {
        this.avatarResId = avatarResId;
        this.avatarUrl = null;
        this.userName = userName;
        this.rating = rating;
        this.timeAgo = timeAgo;
        this.comment = comment;
        this.imageResIds = imageResIds;
        this.imageUrls = new java.util.ArrayList<>();
    }

    public Review(String avatarUrl, String userName, float rating, String timeAgo, String comment, java.util.List<Integer> imageResIds){
        this.avatarUrl = avatarUrl;
        this.avatarResId = 0;
        this.userName = userName;
        this.rating = rating;
        this.timeAgo = timeAgo;
        this.comment = comment;
        this.imageResIds = imageResIds;
        this.imageUrls = new java.util.ArrayList<>();
    }

    public Review(String avatarUrl, String userName, float rating, String timeAgo, String comment, java.util.List<String> imageUrls, boolean isUrlList){
        this.avatarUrl = avatarUrl;
        this.avatarResId = 0;
        this.userName = userName;
        this.rating = rating;
        this.timeAgo = timeAgo;
        this.comment = comment;
        this.imageUrls = imageUrls;
        this.imageResIds = new java.util.ArrayList<>();
    }
} 
