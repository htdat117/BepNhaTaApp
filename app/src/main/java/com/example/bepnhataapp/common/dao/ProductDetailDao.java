package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
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
        if (idxRecipe != -1 && !c.isNull(idxRecipe)) d.setRecipeID(c.getLong(idxRecipe));
        // Nutrition 2 / 4
        int idxCalo2 = c.getColumnIndex("calo2");
        if (idxCalo2 == -1) idxCalo2 = c.getColumnIndex("calo");
        if (idxCalo2 != -1) d.setCalo2(c.getDouble(idxCalo2));
        int idxCalo4 = c.getColumnIndex("calo4");
        if (idxCalo4 != -1) d.setCalo4(c.getDouble(idxCalo4));

        int idxProtein2 = c.getColumnIndex("protein2");
        if (idxProtein2 == -1) idxProtein2 = c.getColumnIndex("protein");
        if (idxProtein2 != -1) d.setProtein2(c.getDouble(idxProtein2));
        int idxProtein4 = c.getColumnIndex("protein4");
        if (idxProtein4 != -1) d.setProtein4(c.getDouble(idxProtein4));

        int idxCarbs2 = c.getColumnIndex("carbs2");
        if (idxCarbs2 == -1) idxCarbs2 = c.getColumnIndex("carbs");
        if (idxCarbs2 != -1) d.setCarbs2(c.getDouble(idxCarbs2));
        int idxCarbs4 = c.getColumnIndex("carbs4");
        if (idxCarbs4 != -1) d.setCarbs4(c.getDouble(idxCarbs4));

        int idxFat2 = c.getColumnIndex("fat2");
        if (idxFat2 == -1) idxFat2 = c.getColumnIndex("fat");
        if (idxFat2 != -1) d.setFat2(c.getDouble(idxFat2));
        int idxFat4 = c.getColumnIndex("fat4");
        if (idxFat4 != -1) d.setFat4(c.getDouble(idxFat4));

        d.setFoodTag(c.getString(c.getColumnIndexOrThrow("foodTag")));
        d.setCuisine(c.getString(c.getColumnIndexOrThrow("cuisine")));
        int idxNutTag = c.getColumnIndex("nutritionTag");
        if (idxNutTag != -1) d.setNutritionTag(c.getString(idxNutTag));

        int idxCook2 = c.getColumnIndex("cookingTimeMinutes2");
        if (idxCook2 == -1) idxCook2 = c.getColumnIndex("cookingTimeMinutes");
        if (idxCook2 != -1) d.setCookingTimeMinutes2(c.getInt(idxCook2));
        int idxCook4 = c.getColumnIndex("cookingTimeMinutes4");
        if (idxCook4 != -1) d.setCookingTimeMinutes4(c.getInt(idxCook4));

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

    public ProductDetail getByRecipeId(long recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCT_DETAILS, null, "recipeID=?", new String[]{String.valueOf(recipeId)}, null, null, null);
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
        cv.put("calo2", d.getCalo2());
        cv.put("calo4", d.getCalo4());
        cv.put("protein2", d.getProtein2());
        cv.put("protein4", d.getProtein4());
        cv.put("carbs2", d.getCarbs2());
        cv.put("carbs4", d.getCarbs4());
        cv.put("fat2", d.getFat2());
        cv.put("fat4", d.getFat4());
        cv.put("foodTag", d.getFoodTag());
        cv.put("cuisine", d.getCuisine());
        cv.put("nutritionTag", d.getNutritionTag());
        cv.put("cookingTimeMinutes2", d.getCookingTimeMinutes2());
        cv.put("cookingTimeMinutes4", d.getCookingTimeMinutes4());
        cv.put("storageGuide", d.getStorageGuide());
        cv.put("expiry", d.getExpiry());
        cv.put("note", d.getNote());
        return cv;
    }
} 
