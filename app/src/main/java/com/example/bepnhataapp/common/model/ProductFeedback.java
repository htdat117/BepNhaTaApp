package com.example.bepnhataapp.common.model;

public class ProductFeedback {
    private long proFeedbackID;
    private long orderLineID;
    private String content;
    private String image; // can be null
    private int rating;
    private String createdAt;

    public ProductFeedback() {}

    public ProductFeedback(long proFeedbackID, long orderLineID, String content, String image, int rating, String createdAt) {
        this.proFeedbackID = proFeedbackID;
        this.orderLineID = orderLineID;
        this.content = content;
        this.image = image;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public long getProFeedbackID() { return proFeedbackID; }
    public void setProFeedbackID(long proFeedbackID) { this.proFeedbackID = proFeedbackID; }

    public long getOrderLineID() { return orderLineID; }
    public void setOrderLineID(long orderLineID) { this.orderLineID = orderLineID; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 
