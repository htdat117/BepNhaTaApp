package com.example.bepnhataapp.common.model;

public class CartDetail {
    private long cartID;
    private long productID;
    private int quantity;

    public CartDetail() {}

    public CartDetail(long cartID, long productID, int quantity) {
        this.cartID = cartID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public long getCartID() { return cartID; }
    public void setCartID(long cartID) { this.cartID = cartID; }

    public long getProductID() { return productID; }
    public void setProductID(long productID) { this.productID = productID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
} 