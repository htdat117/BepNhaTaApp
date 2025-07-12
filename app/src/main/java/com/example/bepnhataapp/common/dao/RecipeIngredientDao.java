package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDao {

    private final DBHelper helper;

    public RecipeIngredientDao(Context ctx) {
        helper = new DBHelper(ctx.getApplicationContext());
    }

    // CRUD operations
    public long insert(RecipeIngredient ri) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_RECIPE_INGREDIENTS, null, toContentValues(ri));
    }

    public int update(RecipeIngredient ri) {
        return helper.getWritableDatabase().update(
            DBHelper.TBL_RECIPE_INGREDIENTS, 
            toContentValues(ri), 
            "ingredientID=? AND recipeID=?", 
            new String[]{String.valueOf(ri.getIngredientID()), String.valueOf(ri.getRecipeID())}
        );
    }

    public int delete(long ingredientID, long recipeID) {
        return helper.getWritableDatabase().delete(
            DBHelper.TBL_RECIPE_INGREDIENTS, 
            "ingredientID=? AND recipeID=?", 
            new String[]{String.valueOf(ingredientID), String.valueOf(recipeID)}
        );
    }

    public List<RecipeIngredient> getAll() {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_RECIPE_INGREDIENTS, null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<RecipeIngredient> getByRecipeID(long recipeID) {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_RECIPE_INGREDIENTS + " WHERE recipeID=?",
            new String[]{String.valueOf(recipeID)}
        );
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        Log.d("DEBUG_DAO", "getByRecipeID: recipeID=" + recipeID + ", count=" + list.size());
        return list;
    }

    public RecipeIngredient get(long ingredientID, long recipeID) {
        Cursor c = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_RECIPE_INGREDIENTS + " WHERE ingredientID=? AND recipeID=?", 
            new String[]{String.valueOf(ingredientID), String.valueOf(recipeID)}
        );
        RecipeIngredient ri = null;
        if (c.moveToFirst()) ri = fromCursor(c);
        c.close();
        return ri;
    }

    // Helpers
    private ContentValues toContentValues(RecipeIngredient ri) {
        ContentValues cv = new ContentValues();
        cv.put("ingredientID", ri.getIngredientID());
        cv.put("recipeID", ri.getRecipeID());
        cv.put("quantity", ri.getQuantity());
        cv.put("nameIngredient", ri.getNameIngredient());
        return cv;
    }

    private RecipeIngredient fromCursor(Cursor cur) {
        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredientID(cur.getLong(cur.getColumnIndexOrThrow("ingredientID")));
        ri.setRecipeID(cur.getLong(cur.getColumnIndexOrThrow("recipeID")));
        ri.setQuantity(cur.getDouble(cur.getColumnIndexOrThrow("quantity")));
        ri.setNameIngredient(cur.getString(cur.getColumnIndexOrThrow("nameIngredient")));
        return ri;
    }
} 
