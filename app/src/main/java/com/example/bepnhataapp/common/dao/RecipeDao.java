package com.example.bepnhataapp.common.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class RecipeDao {

    private final DBHelper helper;

    public RecipeDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public List<RecipeEntity> getAllRecipes() {
        List<RecipeEntity> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM RECIPES", null);
            if (c.moveToFirst()) {
                do {
                    list.add(fromCursor(c));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    private RecipeEntity fromCursor(Cursor cur) {
        RecipeEntity r = new RecipeEntity();
        r.setRecipeID(cur.getInt(cur.getColumnIndexOrThrow("recipeID")));
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