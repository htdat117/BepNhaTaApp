package com.example.bepnhataapp.common.model;

import java.io.Serializable;

/**
 * Represents an ingredient that belongs to a product (meal kit).
 * Composite primary-key: (productID, ingredientID)
 */
public class ProductIngredient implements Serializable {
    private long productID;
    private long ingredientID;

    // Quantity per ingredient for different kit sizes (2-person / 4-person)
    private int quantity2;
    private int quantity4;

    public ProductIngredient() {
    }

    public ProductIngredient(long productID, long ingredientID, int quantity2, int quantity4) {
        this.productID = productID;
        this.ingredientID = ingredientID;
        this.quantity2 = quantity2;
        this.quantity4 = quantity4;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public long getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(long ingredientID) {
        this.ingredientID = ingredientID;
    }

    public int getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(int quantity2) {
        this.quantity2 = quantity2;
    }

    public int getQuantity4() {
        return quantity4;
    }

    public void setQuantity4(int quantity4) {
        this.quantity4 = quantity4;
    }

    // ----------------- Backward compatibility ----------------- //
    public int getQuantity() {
        return quantity2;
    }

    public void setQuantity(int quantity) {
        this.quantity2 = quantity;
    }
    // ---------------------------------------------------------- //
}
