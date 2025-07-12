package com.example.bepnhataapp.common.model;

public class PointLog {
    private long id;
    private long customerId;
    private String action; // tên hoạt động
    private int point; // điểm cộng/trừ
    private String description; // mô tả
    private String createdAt; // thời gian

    public PointLog() {}
    public PointLog(long customerId, String action, int point, String description, String createdAt) {
        this.customerId = customerId;
        this.action = action;
        this.point = point;
        this.description = description;
        this.createdAt = createdAt;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 
