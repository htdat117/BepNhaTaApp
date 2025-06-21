package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.database.DBHelper;
import com.example.bepnhataapp.common.model.ProductDetail;

public class ProductDetailDao {
    private final DBHelper dbHelper;

    public ProductDetailDao(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    private ProductDetail cursorToDetail(Cursor c) {
        ProductDetail d = new ProductDetail();
        d.setProductID(c.getLong(c.getColumnIndexOrThrow("productID")));
        int idxRecipe = c.getColumnIndex("recipeID");
        if (!c.isNull(idxRecipe)) d.setRecipeID(c.getLong(idxRecipe));
        d.setCalo(c.getDouble(c.getColumnIndexOrThrow("calo")));
        d.setProtein(c.getDouble(c.getColumnIndexOrThrow("protein")));
        d.setCarbs(c.getDouble(c.getColumnIndexOrThrow("carbs")));
        d.setFat(c.getDouble(c.getColumnIndexOrThrow("fat")));
        d.setFoodTag(c.getString(c.getColumnIndexOrThrow("foodTag")));
        d.setCuisine(c.getString(c.getColumnIndexOrThrow("cuisine")));
        d.setCookingTimeMinutes(c.getInt(c.getColumnIndexOrThrow("cookingTimeMinutes")));
        d.setStorageGuide(c.getString(c.getColumnIndexOrThrow("storageGuide")));
        d.setExpiry(c.getString(c.getColumnIndexOrThrow("expiry")));
        d.setNote(c.getString(c.getColumnIndexOrThrow("note")));
        return d;
    }

    public ProductDetail getByProductId(long productID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCT_DETAILS, null, "productID=?", new String[]{String.valueOf(productID)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            ProductDetail d = cursorToDetail(c);
            c.close();
            return d;
        }
        return null;
    }

    public long insert(ProductDetail d) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(d);
        return db.insert(DBHelper.TBL_PRODUCT_DETAILS, null, cv);
    }

    public int update(ProductDetail d) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(d);
        return db.update(DBHelper.TBL_PRODUCT_DETAILS, cv, "productID=?", new String[]{String.valueOf(d.getProductID())});
    }

    public int delete(long productID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TBL_PRODUCT_DETAILS, "productID=?", new String[]{String.valueOf(productID)});
    }

    private ContentValues toCV(ProductDetail d) {
        ContentValues cv = new ContentValues();
        cv.put("productID", d.getProductID());
        if (d.getRecipeID() != null) cv.put("recipeID", d.getRecipeID()); else cv.putNull("recipeID");
        cv.put("calo", d.getCalo());
        cv.put("protein", d.getProtein());
        cv.put("carbs", d.getCarbs());
        cv.put("fat", d.getFat());
        cv.put("foodTag", d.getFoodTag());
        cv.put("cuisine", d.getCuisine());
        cv.put("cookingTimeMinutes", d.getCookingTimeMinutes());
        cv.put("storageGuide", d.getStorageGuide());
        cv.put("expiry", d.getExpiry());
        cv.put("note", d.getNote());
        return cv;
    }
} 