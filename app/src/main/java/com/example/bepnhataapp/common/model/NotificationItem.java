package com.example.bepnhataapp.common.model;

public class NotificationItem {
    private String id;
    private String title;
    private String body;
    private long timestamp;
    private boolean read;
    private String status;

    public NotificationItem(String id, String title, String body, long timestamp, boolean read, String status){
        this.id=id;
        this.title=title;
        this.body=body;
        this.timestamp=timestamp;
        this.read=read;
        this.status=status;
    }
    public String getId(){return id;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
    public long getTimestamp(){return timestamp;}
    public boolean isRead(){return read;}
    public void setRead(boolean r){this.read=r;}
    public String getStatus(){return status;}
} 
