package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class ProductIngredient implements Serializable {
    private long productID;
    private long ingredientID;
    private int quantity;

    public ProductIngredient() {}

    public ProductIngredient(long productID, long ingredientID, int quantity) {
        this.productID = productID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
    }

    public long getProductID() { return productID; }
    public void setProductID(long productID) { this.productID = productID; }

    public long getIngredientID() { return ingredientID; }
    public void setIngredientID(long ingredientID) { this.ingredientID = ingredientID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}