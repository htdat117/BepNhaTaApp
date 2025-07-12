package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class RecipeComment implements Serializable {
    private long recipeCommentID;
    private long recipeID;
    private long customerID;
    private String content;
    private String createdAt;
    private Long parentCommentID;
    private int usefulness;

    public RecipeComment() {}

    public RecipeComment(long recipeCommentID, long recipeID, long customerID, String content, String createdAt, Long parentCommentID, int usefulness) {
        this.recipeCommentID = recipeCommentID;
        this.recipeID = recipeID;
        this.customerID = customerID;
        this.content = content;
        this.createdAt = createdAt;
        this.parentCommentID = parentCommentID;
        this.usefulness = usefulness;
    }

    public long getRecipeCommentID() { return recipeCommentID; }
    public void setRecipeCommentID(long recipeCommentID) { this.recipeCommentID = recipeCommentID; }
    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }
    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Long getParentCommentID() { return parentCommentID; }
    public void setParentCommentID(Long parentCommentID) { this.parentCommentID = parentCommentID; }
    public int getUsefulness() { return usefulness; }
    public void setUsefulness(int usefulness) { this.usefulness = usefulness; }
}
