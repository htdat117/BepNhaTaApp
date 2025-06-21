package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.database.DBHelper;
import com.example.bepnhataapp.common.model.ProductIngredient;

import java.util.ArrayList;
import java.util.List;

public class ProductIngredientDao {
    private final DBHelper dbHelper;

    public ProductIngredientDao(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    private ProductIngredient cursorToPI(Cursor c) {
        ProductIngredient pi = new ProductIngredient();
        pi.setProductID(c.getLong(c.getColumnIndexOrThrow("productID")));
        pi.setIngredientID(c.getLong(c.getColumnIndexOrThrow("ingredientID")));
        pi.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
        return pi;
    }

    public long insert(ProductIngredient pi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(pi);
        return db.insert(DBHelper.TBL_PRODUCT_INGREDIENTS, null, cv);
    }

    public int update(ProductIngredient pi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(pi);
        return db.update(DBHelper.TBL_PRODUCT_INGREDIENTS, cv, "productID=? AND ingredientID=?", new String[]{String.valueOf(pi.getProductID()), String.valueOf(pi.getIngredientID())});
    }

    public int delete(long productID, long ingredientID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TBL_PRODUCT_INGREDIENTS, "productID=? AND ingredientID=?", new String[]{String.valueOf(productID), String.valueOf(ingredientID)});
    }

    public List<ProductIngredient> getIngredientsOfProduct(long productID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCT_INGREDIENTS, null, "productID=?", new String[]{String.valueOf(productID)}, null, null, null);
        List<ProductIngredient> list = new ArrayList<>();
        if (c.moveToFirst()) { do { list.add(cursorToPI(c)); } while (c.moveToNext()); }
        c.close();
        return list;
    }

    public List<ProductIngredient> getProductsUsingIngredient(long ingredientID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCT_INGREDIENTS, null, "ingredientID=?", new String[]{String.valueOf(ingredientID)}, null, null, null);
        List<ProductIngredient> list = new ArrayList<>();
        if (c.moveToFirst()) { do { list.add(cursorToPI(c)); } while (c.moveToNext()); }
        c.close();
        return list;
    }

    private ContentValues toCV(ProductIngredient pi) {
        ContentValues cv = new ContentValues();
        cv.put("productID", pi.getProductID());
        cv.put("ingredientID", pi.getIngredientID());
        cv.put("quantity", pi.getQuantity());
        return cv;
    }
}