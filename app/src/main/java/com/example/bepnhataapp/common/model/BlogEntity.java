package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class BlogEntity implements Serializable {
    private long blogID;
    private String title;
    private String content;
    private String authorName;
    private String createdAt;
    private String imageThumb; // URL or base64 string stored as TEXT
    private String status;     // draft / published
    private String tag;
    private int likes = 0;

    public BlogEntity() { }

    public BlogEntity(long blogID, String title, String content, String authorName, String createdAt, String imageThumb, String status, String tag) {
        this.blogID = blogID;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.imageThumb = imageThumb;
        this.status = status;
        this.tag = tag;
    }

    // Getters & setters
    public long getBlogID() { return blogID; }
    public void setBlogID(long blogID) { this.blogID = blogID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getImageThumb() { return imageThumb; }
    public void setImageThumb(String imageThumb) { this.imageThumb = imageThumb; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
} 
