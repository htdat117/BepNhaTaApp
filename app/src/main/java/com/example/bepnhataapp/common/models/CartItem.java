package com.example.bepnhataapp.common.models;

public class CartItem {
    private String title;
    private String price;
    private String serving;
    // Thêm các trường khác nếu cần
    public CartItem(String title, String price, String serving) {
        this.title = title;
        this.price = price;
        this.serving = serving;
    }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getServing() { return serving; }
    public void setServing(String serving) { this.serving = serving; }
} 