package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.database.DBHelper;
import com.example.bepnhataapp.common.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDao {

    private final DBHelper helper;

    public RecipeDao(Context ctx) {
        helper = new DBHelper(ctx.getApplicationContext());
    }

    // CRUD operations
    public long insert(Recipe r) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_RECIPES, null, toContentValues(r));
    }

    public int update(Recipe r) {
        return helper.getWritableDatabase().update(DBHelper.TBL_RECIPES, toContentValues(r), "recipeID=?", new String[]{String.valueOf(r.getRecipeID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_RECIPES, "recipeID=?", new String[]{String.valueOf(id)});
    }

    public List<Recipe> getAll() {
        List<Recipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_RECIPES + " ORDER BY recipeID DESC", null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public Recipe get(long id) {
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_RECIPES + " WHERE recipeID=?", new String[]{String.valueOf(id)});
        Recipe r = null;
        if (c.moveToFirst()) r = fromCursor(c);
        c.close();
        return r;
    }

    // Helpers
    private ContentValues toContentValues(Recipe r) {
        ContentValues cv = new ContentValues();
        cv.put("recipeName", r.getRecipeName());
        cv.put("description", r.getDescription());
        cv.put("tag", r.getTag());
        cv.put("createdAt", r.getCreatedAt());
        cv.put("imageThumb", r.getImageThumb());
        cv.put("category", r.getCategory());
        cv.put("commentAmount", r.getCommentAmount());
        cv.put("likeAmount", r.getLikeAmount());
        cv.put("sectionAmount", r.getSectionAmount());
        return cv;
    }

    private Recipe fromCursor(Cursor cur) {
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
        return r;
    }
} 