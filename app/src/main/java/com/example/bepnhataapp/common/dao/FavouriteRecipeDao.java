package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.FavouriteRecipe;
import com.example.bepnhataapp.common.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecipeDao {

    private final DBHelper helper;

    public FavouriteRecipeDao(Context ctx) {
        helper = new DBHelper(ctx.getApplicationContext());
    }

    // CRUD operations
    public long insert(FavouriteRecipe fr) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_FAVOURITE_RECIPES, null, toContentValues(fr));
    }

    public int update(FavouriteRecipe fr) {
        return helper.getWritableDatabase().update(
            DBHelper.TBL_FAVOURITE_RECIPES, 
            toContentValues(fr), 
            "recipeID=? AND customerID=?", 
            new String[]{String.valueOf(fr.getRecipeID()), String.valueOf(fr.getCustomerID())}
        );
    }

    public int delete(long recipeID, long customerID) {
        return helper.getWritableDatabase().delete(
            DBHelper.TBL_FAVOURITE_RECIPES, 
            "recipeID=? AND customerID=?", 
            new String[]{String.valueOf(recipeID), String.valueOf(customerID)}
        );
    }

    public List<FavouriteRecipe> getAll() {
        List<FavouriteRecipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_FAVOURITE_RECIPES, null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<FavouriteRecipe> getByCustomerID(long customerID) {
        List<FavouriteRecipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_FAVOURITE_RECIPES + " WHERE customerID=?", 
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

    public List<Recipe> getFavouriteRecipes(long customerID) {
        List<Recipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT r.* FROM " + DBHelper.TBL_RECIPES + " r " +
            "JOIN " + DBHelper.TBL_FAVOURITE_RECIPES + " f ON r.recipeID = f.recipeID " +
            "WHERE f.customerID=?", 
            new String[]{String.valueOf(customerID)}
        );
        if (cur.moveToFirst()) {
            do {
                Recipe r = new Recipe();
                r.setRecipeID(cur.getLong(cur.getColumnIndexOrThrow("recipeID")));
                r.setRecipeName(cur.getString(cur.getColumnIndexOrThrow("recipeName")));
                r.setDescription(cur.getString(cur.getColumnIndexOrThrow("description")));
                r.setTag(cur.getString(cur.getColumnIndexOrThrow("tag")));
                r.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
                r.setImageThumb(cur.getString(cur.getColumnIndexOrThrow("imageThumb")));
                r.setCategory(cur.getString(cur.getColumnIndexOrThrow("category")));
                r.setCommentAmount(cur.getInt(cur.getColumnIndexOrThrow("commentAmount")));
                r.setLikeAmount(cur.getInt(cur.getColumnIndexOrThrow("likeAmount")));
                r.setSectionAmount(cur.getInt(cur.getColumnIndexOrThrow("sectionAmount")));
                list.add(r);
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public FavouriteRecipe get(long recipeID, long customerID) {
        Cursor c = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_FAVOURITE_RECIPES + " WHERE recipeID=? AND customerID=?", 
            new String[]{String.valueOf(recipeID), String.valueOf(customerID)}
        );
        FavouriteRecipe fr = null;
        if (c.moveToFirst()) fr = fromCursor(c);
        c.close();
        return fr;
    }

    // Helpers
    private ContentValues toContentValues(FavouriteRecipe fr) {
        ContentValues cv = new ContentValues();
        cv.put("recipeID", fr.getRecipeID());
        cv.put("customerID", fr.getCustomerID());
        cv.put("createdAt", fr.getCreatedAt());
        return cv;
    }

    private FavouriteRecipe fromCursor(Cursor cur) {
        FavouriteRecipe fr = new FavouriteRecipe();
        fr.setRecipeID(cur.getLong(cur.getColumnIndexOrThrow("recipeID")));
        fr.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        fr.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        return fr;
    }
} 
