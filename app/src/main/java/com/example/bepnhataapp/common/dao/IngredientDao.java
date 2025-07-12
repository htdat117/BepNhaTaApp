package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientDao {
    private final DBHelper dbHelper;

    public IngredientDao(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    private Ingredient cursorToIngredient(Cursor c) {
        Ingredient i = new Ingredient();
        i.setIngredientID(c.getLong(c.getColumnIndexOrThrow("ingredientID")));
        i.setIngredientName(c.getString(c.getColumnIndexOrThrow("ingredientName")));
        i.setUnit(c.getString(c.getColumnIndexOrThrow("unit")));
        int colImage = c.getColumnIndex("image");
        if(colImage != -1){
            // Try to get as String first (URL), else blob
            try {
                String link = c.getString(colImage);
                if(link!=null){
                    i.setImageLink(link);
                }
            } catch (Exception e){
                try{
                    i.setImage(c.getBlob(colImage));
                }catch (Exception ignored){}
            }
        }
        return i;
    }

    public long insert(Ingredient i) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(i, false);
        return db.insert(DBHelper.TBL_INGREDIENTS, null, cv);
    }

    public int update(Ingredient i) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(i, true);
        return db.update(DBHelper.TBL_INGREDIENTS, cv, "ingredientID=?", new String[]{String.valueOf(i.getIngredientID())});
    }

    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TBL_INGREDIENTS, "ingredientID=?", new String[]{String.valueOf(id)});
    }

    public Ingredient getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_INGREDIENTS, null, "ingredientID=?", new String[]{String.valueOf(id)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            Ingredient i = cursorToIngredient(c);
            c.close();
            return i;
        }
        return null;
    }

    public List<Ingredient> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_INGREDIENTS, null, null, null, null, null, "ingredientName ASC");
        List<Ingredient> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do { list.add(cursorToIngredient(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    /**
     * Trả về danh sách nguyên liệu được sử dụng trong công thức (recipeID).
     */
    public List<Ingredient> getByRecipe(long recipeID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT i.* FROM " + DBHelper.TBL_INGREDIENTS + " i " +
                "JOIN " + DBHelper.TBL_RECIPE_INGREDIENTS + " ri ON i.ingredientID = ri.ingredientID " +
                "WHERE ri.recipeID = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(recipeID)});
        List<Ingredient> list = new ArrayList<>();
        if(c.moveToFirst()){
            do{ list.add(cursorToIngredient(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private ContentValues toCV(Ingredient i, boolean includeId) {
        ContentValues cv = new ContentValues();
        if (includeId) cv.put("ingredientID", i.getIngredientID());
        cv.put("ingredientName", i.getIngredientName());
        cv.put("unit", i.getUnit());
        cv.put("image", i.getImage());
        return cv;
    }
} 
