package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.RecipeComment;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommentDao {

    private final DBHelper helper;

    public RecipeCommentDao(Context ctx) { this.helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(RecipeComment c) { return helper.getWritableDatabase().insert(DBHelper.TBL_RECIPE_COMMENTS, null, toCV(c)); }

    public int update(RecipeComment c) {
        return helper.getWritableDatabase().update(DBHelper.TBL_RECIPE_COMMENTS, toCV(c), "recipeCommentID=?", new String[]{String.valueOf(c.getRecipeCommentID())});
    }

    public int delete(long commentID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_RECIPE_COMMENTS, "recipeCommentID=?", new String[]{String.valueOf(commentID)});
    }

    public List<RecipeComment> getByRecipe(long recipeID) {
        List<RecipeComment> list = new ArrayList<>();
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_RECIPE_COMMENTS + " WHERE recipeID=? ORDER BY createdAt ASC", new String[]{String.valueOf(recipeID)});
        if (c.moveToFirst()) {
            do { list.add(fromCursor(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private ContentValues toCV(RecipeComment c) {
        ContentValues v = new ContentValues();
        if (c.getRecipeCommentID() > 0) v.put("recipeCommentID", c.getRecipeCommentID());
        v.put("recipeID", c.getRecipeID());
        v.put("customerID", c.getCustomerID());
        v.put("content", c.getContent());
        v.put("createdAt", c.getCreatedAt());
        if (c.getParentCommentID() != null) v.put("parentCommentID", c.getParentCommentID()); else v.putNull("parentCommentID");
        v.put("usefulness", c.getUsefulness());
        return v;
    }

    private RecipeComment fromCursor(Cursor cur) {
        RecipeComment rc = new RecipeComment();
        rc.setRecipeCommentID(cur.getLong(cur.getColumnIndexOrThrow("recipeCommentID")));
        rc.setRecipeID(cur.getLong(cur.getColumnIndexOrThrow("recipeID")));
        rc.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        rc.setContent(cur.getString(cur.getColumnIndexOrThrow("content")));
        rc.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        if (!cur.isNull(cur.getColumnIndexOrThrow("parentCommentID")))
            rc.setParentCommentID(cur.getLong(cur.getColumnIndexOrThrow("parentCommentID")));
        rc.setUsefulness(cur.getInt(cur.getColumnIndexOrThrow("usefulness")));
        return rc;
    }
} 
