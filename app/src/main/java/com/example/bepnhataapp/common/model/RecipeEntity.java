package com.example.bepnhataapp.common.model;

public class RecipeEntity {
    private int recipeID;
    private String recipeName;
    private String description;
    private String tag;
    private String createdAt;
    private String imageThumb;
    private String category;
    private int commentAmount;
    private int likeAmount;
    private int sectionAmount;

    // Getters
    public int getRecipeID() { return recipeID; }
    public String getRecipeName() { return recipeName; }
    public String getDescription() { return description; }
    public String getTag() { return tag; }
    public String getCreatedAt() { return createdAt; }
    public String getImageThumb() { return imageThumb; }
    public String getCategory() { return category; }
    public int getCommentAmount() { return commentAmount; }
    public int getLikeAmount() { return likeAmount; }
    public int getSectionAmount() { return sectionAmount; }

    // Setters
    public void setRecipeID(int recipeID) { this.recipeID = recipeID; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public void setDescription(String description) { this.description = description; }
    public void setTag(String tag) { this.tag = tag; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setImageThumb(String imageThumb) { this.imageThumb = imageThumb; }
    public void setCategory(String category) { this.category = category; }
    public void setCommentAmount(int commentAmount) { this.commentAmount = commentAmount; }
    public void setLikeAmount(int likeAmount) { this.likeAmount = likeAmount; }
    public void setSectionAmount(int sectionAmount) { this.sectionAmount = sectionAmount; }
} 
