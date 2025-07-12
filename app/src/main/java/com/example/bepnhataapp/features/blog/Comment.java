package com.example.bepnhataapp.features.blog;

public class Comment {
    private String userName;
    private String time;
    private String commentText;
    private int likes;

    public Comment(String userName, String time, String commentText, int likes) {
        this.userName = userName;
        this.time = time;
        this.commentText = commentText;
        this.likes = likes;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public String getCommentText() {
        return commentText;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
} 
