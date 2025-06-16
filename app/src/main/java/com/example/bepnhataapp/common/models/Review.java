package com.example.bepnhataapp.common.models;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {
    public int avatarResId;
    public String userName;
    public float rating;
    public String timeAgo;
    public String comment;
    public List<Integer> imageResIds;

    public Review(int avatarResId, String userName, float rating, String timeAgo, String comment, List<Integer> imageResIds) {
        this.avatarResId = avatarResId;
        this.userName = userName;
        this.rating = rating;
        this.timeAgo = timeAgo;
        this.comment = comment;
        this.imageResIds = imageResIds;
    }
} 