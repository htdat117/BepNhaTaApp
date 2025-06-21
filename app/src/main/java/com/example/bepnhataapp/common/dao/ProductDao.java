package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private final DBHelper dbHelper;

    public ProductDao(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    private Product cursorToProduct(Cursor c) {
        Product p = new Product();
        p.setProductID(c.getLong(c.getColumnIndexOrThrow("productID")));
        p.setProductName(c.getString(c.getColumnIndexOrThrow("productName")));
        p.setProductDescription(c.getString(c.getColumnIndexOrThrow("productDescription")));
        p.setProductPrice(c.getInt(c.getColumnIndexOrThrow("productPrice")));
        p.setSalePercent(c.getInt(c.getColumnIndexOrThrow("salePercent")));
        p.setProductThumb(c.getBlob(c.getColumnIndexOrThrow("productThumb")));
        p.setCommentAmount(c.getInt(c.getColumnIndexOrThrow("commentAmount")));
        p.setCategory(c.getString(c.getColumnIndexOrThrow("category")));
        p.setInventory(c.getInt(c.getColumnIndexOrThrow("inventory")));
        p.setSoldQuantity(c.getInt(c.getColumnIndexOrThrow("soldQuantity")));
        p.setAvgRating(c.getDouble(c.getColumnIndexOrThrow("avgRating")));
        p.setStatus(c.getString(c.getColumnIndexOrThrow("status")));
        p.setCreatedDate(c.getString(c.getColumnIndexOrThrow("createdDate")));
        p.setUpdatedDate(c.getString(c.getColumnIndexOrThrow("updatedDate")));
        return p;
    }

    public long insert(Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toContentValues(p, false);
        return db.insert(DBHelper.TBL_PRODUCTS, null, cv);
    }

    public int update(Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toContentValues(p, true);
        return db.update(DBHelper.TBL_PRODUCTS, cv, "productID=?", new String[]{String.valueOf(p.getProductID())});
    }

    public int delete(long productID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TBL_PRODUCTS, "productID=?", new String[]{String.valueOf(productID)});
    }

    public Product getById(long productID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, "productID=?", new String[]{String.valueOf(productID)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            Product p = cursorToProduct(c);
            c.close();
            return p;
        }
        return null;
    }

    public List<Product> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, null, null, null, null, "createdDate DESC");
        List<Product> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do { list.add(cursorToProduct(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Product> getByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, "category=?", new String[]{category}, null, null, "createdDate DESC");
        List<Product> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do { list.add(cursorToProduct(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private ContentValues toContentValues(Product p, boolean includeId) {
        ContentValues cv = new ContentValues();
        if (includeId) cv.put("productID", p.getProductID());
        cv.put("productName", p.getProductName());
        cv.put("productDescription", p.getProductDescription());
        cv.put("productPrice", p.getProductPrice());
        cv.put("salePercent", p.getSalePercent());
        cv.put("productThumb", p.getProductThumb());
        cv.put("commentAmount", p.getCommentAmount());
        cv.put("category", p.getCategory());
        cv.put("inventory", p.getInventory());
        cv.put("soldQuantity", p.getSoldQuantity());
        cv.put("avgRating", p.getAvgRating());
        cv.put("status", p.getStatus());
        cv.put("createdDate", p.getCreatedDate());
        cv.put("updatedDate", p.getUpdatedDate());
        return cv;
    }
} 