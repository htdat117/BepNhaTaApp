package com.example.bepnhataapp.common.model;

public class Recipe {
    private long recipeID;
    private String recipeName;
    private String description;
    private String tag;
    private String createdAt;
    private String imageThumb;
    private String category;
    private int commentAmount;
    private int likeAmount;
    private int sectionAmount;

    public Recipe() {}

    // getters & setters
    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getImageThumb() { return imageThumb; }
    public void setImageThumb(String imageThumb) { this.imageThumb = imageThumb; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCommentAmount() { return commentAmount; }
    public void setCommentAmount(int commentAmount) { this.commentAmount = commentAmount; }

    public int getLikeAmount() { return likeAmount; }
    public void setLikeAmount(int likeAmount) { this.likeAmount = likeAmount; }

    public int getSectionAmount() { return sectionAmount; }
    public void setSectionAmount(int sectionAmount) { this.sectionAmount = sectionAmount; }
} 