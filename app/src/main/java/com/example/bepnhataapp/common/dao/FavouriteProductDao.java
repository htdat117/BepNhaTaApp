package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.FavouriteProduct;

import java.util.ArrayList;
import java.util.List;

public class FavouriteProductDao {
    private final DBHelper dbHelper;

    public FavouriteProductDao(Context ctx) { this.dbHelper = new DBHelper(ctx); }

    private FavouriteProduct cursorToFP(Cursor c) {
        return new FavouriteProduct(
                c.getLong(c.getColumnIndexOrThrow("productID")),
                c.getLong(c.getColumnIndexOrThrow("customerID")),
                c.getString(c.getColumnIndexOrThrow("createdAt"))
        );
    }

    public long insert(FavouriteProduct fp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(fp);
        return db.insert(DBHelper.TBL_FAVOURITE_PRODUCTS, null, cv);
    }

    public int delete(long productID, long customerID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TBL_FAVOURITE_PRODUCTS, "productID=? AND customerID=?", new String[]{String.valueOf(productID), String.valueOf(customerID)});
    }

    public List<FavouriteProduct> getByCustomer(long customerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_FAVOURITE_PRODUCTS, null, "customerID=?", new String[]{String.valueOf(customerID)}, null, null, "createdAt DESC");
        List<FavouriteProduct> list = new ArrayList<>();
        if (c.moveToFirst()) { do { list.add(cursorToFP(c)); } while (c.moveToNext()); }
        c.close();
        return list;
    }

    public boolean isFavourite(long productID, long customerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT 1 FROM " + DBHelper.TBL_FAVOURITE_PRODUCTS + " WHERE productID=? AND customerID=? LIMIT 1", new String[]{String.valueOf(productID), String.valueOf(customerID)});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    private ContentValues toCV(FavouriteProduct fp) {
        ContentValues cv = new ContentValues();
        cv.put("productID", fp.getProductID());
        cv.put("customerID", fp.getCustomerID());
        cv.put("createdAt", fp.getCreatedAt());
        return cv;
    }
} 
