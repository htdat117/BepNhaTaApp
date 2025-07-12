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
            // Chỉ lấy các cột cần hiển thị, tránh nạp toàn bộ blob/column lớn gây CursorWindow quá lớn
            String sql = "SELECT recipeID, recipeName, description, tag, createdAt, imageThumb, " +
                    "category, commentAmount, likeAmount, sectionAmount FROM RECIPES";
            c = db.rawQuery(sql, null);
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
        // Gán mặc định tag/description/category nếu thiếu để filter lợi ích sức khỏe luôn có kết quả
        String allTags = "Bổ máu,Tốt tim,Giảm cân,Giải độc,Tốt xương,Đẹp da,Dễ ngủ";
        for (int i = 0; i < list.size(); i++) {
            RecipeEntity r = list.get(i);
            if (r.getTag() == null || r.getTag().trim().isEmpty()) r.setTag(allTags);
            if (r.getDescription() == null || r.getDescription().trim().isEmpty()) r.setDescription(allTags + " cho sức khỏe");
            if (r.getCategory() == null || r.getCategory().trim().isEmpty()) r.setCategory("Món chính");
        }
        return list;
    }

    /**
     * Lấy tối đa <code>limit</code> công thức thuộc category chỉ định (không phân biệt hoa thường).
     */
    public List<RecipeEntity> getRecipesByCategory(String category, int limit) {
        List<RecipeEntity> list = new ArrayList<>();
        Cursor c = null;
        try {
            String sql = "SELECT recipeID, recipeName, description, tag, createdAt, imageThumb, " +
                    "category, commentAmount, likeAmount, sectionAmount " +
                    "FROM RECIPES WHERE lower(category)=lower(?) ORDER BY recipeID DESC LIMIT ?";
            c = helper.getReadableDatabase().rawQuery(sql, new String[]{category, String.valueOf(limit)});
            if (c.moveToFirst()) {
                do { list.add(fromCursor(c)); } while (c.moveToNext());
            }
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    /**
     * Trả về danh sách category duy nhất (không null). Dùng để render list button.
     */
    public List<String> getAllCategories() {
        List<String> list = new ArrayList<>();
        Cursor c = null;
        try {
            String sql = "SELECT DISTINCT category FROM RECIPES WHERE category IS NOT NULL ORDER BY category";
            c = helper.getReadableDatabase().rawQuery(sql, null);
            if (c.moveToFirst()) {
                do { list.add(c.getString(0)); } while (c.moveToNext());
            }
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    public RecipeEntity getById(long id){
        Cursor c = null;
        try{
            String sql = "SELECT recipeID, recipeName, description, tag, createdAt, imageThumb, category, commentAmount, likeAmount, sectionAmount FROM RECIPES WHERE recipeID=? LIMIT 1";
            c = helper.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(id)});
            if(c.moveToFirst()) return fromCursor(c);
        }finally{ if(c!=null) c.close(); }
        return null;
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
