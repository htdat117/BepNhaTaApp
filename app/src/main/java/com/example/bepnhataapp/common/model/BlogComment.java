package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class BlogComment implements Serializable {
    private long blogCommentID;
    private long blogID;
    private long customerID;
    private String content;
    private String createdAt;
    private Long parentCommentID; // nullable
    private int usefulness;

    public BlogComment() {}

    public BlogComment(long blogCommentID, long blogID, long customerID, String content, String createdAt, Long parentCommentID, int usefulness) {
        this.blogCommentID = blogCommentID;
        this.blogID = blogID;
        this.customerID = customerID;
        this.content = content;
        this.createdAt = createdAt;
        this.parentCommentID = parentCommentID;
        this.usefulness = usefulness;
    }

    // getters and setters
    public long getBlogCommentID() { return blogCommentID; }
    public void setBlogCommentID(long blogCommentID) { this.blogCommentID = blogCommentID; }

    public long getBlogID() { return blogID; }
    public void setBlogID(long blogID) { this.blogID = blogID; }

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
