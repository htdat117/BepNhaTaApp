package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.RecipeDownload;

import java.util.ArrayList;
import java.util.List;

public class RecipeDownloadDao {

    private final DBHelper helper;

    public RecipeDownloadDao(Context ctx) {
        helper = new DBHelper(ctx.getApplicationContext());
    }

    // CRUD operations
    public long insert(RecipeDownload rd) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_RECIPE_DOWNLOAD, null, toContentValues(rd));
    }

    public int update(RecipeDownload rd) {
        return helper.getWritableDatabase().update(
            DBHelper.TBL_RECIPE_DOWNLOAD, 
            toContentValues(rd), 
            "customerID=? AND recipeID=?", 
            new String[]{String.valueOf(rd.getCustomerID()), String.valueOf(rd.getRecipeID())}
        );
    }

    public int delete(long customerID, long recipeID) {
        return helper.getWritableDatabase().delete(
            DBHelper.TBL_RECIPE_DOWNLOAD, 
            "customerID=? AND recipeID=?", 
            new String[]{String.valueOf(customerID), String.valueOf(recipeID)}
        );
    }

    public List<RecipeDownload> getAll() {
        List<RecipeDownload> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_RECIPE_DOWNLOAD, null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<RecipeDownload> getByCustomerID(long customerID) {
        List<RecipeDownload> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_RECIPE_DOWNLOAD + " WHERE customerID=?", 
            new String[]{String.valueOf(customerID)}
        );
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public RecipeDownload get(long customerID, long recipeID) {
        Cursor c = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_RECIPE_DOWNLOAD + " WHERE customerID=? AND recipeID=?", 
            new String[]{String.valueOf(customerID), String.valueOf(recipeID)}
        );
        RecipeDownload rd = null;
        if (c.moveToFirst()) rd = fromCursor(c);
        c.close();
        return rd;
    }

    // Helpers
    private ContentValues toContentValues(RecipeDownload rd) {
        ContentValues cv = new ContentValues();
        cv.put("customerID", rd.getCustomerID());
        cv.put("recipeID", rd.getRecipeID());
        cv.put("downloadedAt", rd.getDownloadedAt());
        return cv;
    }

    private RecipeDownload fromCursor(Cursor cur) {
        RecipeDownload rd = new RecipeDownload();
        rd.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        rd.setRecipeID(cur.getLong(cur.getColumnIndexOrThrow("recipeID")));
        rd.setDownloadedAt(cur.getString(cur.getColumnIndexOrThrow("downloadedAt")));
        return rd;
    }
} 
