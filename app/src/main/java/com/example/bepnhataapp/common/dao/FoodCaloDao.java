package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.FoodCalo;

import java.util.ArrayList;
import java.util.List;

public class FoodCaloDao {
    private final DBHelper dbHelper;

    public FoodCaloDao(Context ctx) { this.dbHelper = new DBHelper(ctx); }

    private FoodCalo cursorToFC(Cursor c) {
        FoodCalo f = new FoodCalo();
        f.setFoodCaloID(c.getLong(c.getColumnIndexOrThrow("foodCaloID")));
        f.setFoodName(c.getString(c.getColumnIndexOrThrow("foodName")));
        f.setFoodThumb(c.getString(c.getColumnIndexOrThrow("foodThumb")));
        f.setCaloPerOneHundredGrams(c.getInt(c.getColumnIndexOrThrow("caloPerOneHundredGrams")));
        return f;
    }

    public long insert(FoodCalo f) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toCV(f, false);
        return db.insert(DBHelper.TBL_FOOD_CALO, null, cv);
    }

    public List<FoodCalo> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_FOOD_CALO, null, null, null, null, null, "foodName ASC");
        List<FoodCalo> list = new ArrayList<>();
        if (c.moveToFirst()) { do { list.add(cursorToFC(c)); } while (c.moveToNext()); }
        c.close();
        return list;
    }

    /**
     * Tìm kiếm thực phẩm theo tên chính xác
     */
    public FoodCalo findByName(String foodName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_FOOD_CALO, null, 
                           "foodName = ?", new String[]{foodName}, 
                           null, null, null);
        FoodCalo result = null;
        if (c.moveToFirst()) {
            result = cursorToFC(c);
        }
        c.close();
        return result;
    }

    /**
     * Tìm kiếm thực phẩm theo từ khóa (LIKE)
     */
    public List<FoodCalo> searchByName(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String searchPattern = "%" + keyword + "%";
        Cursor c = db.query(DBHelper.TBL_FOOD_CALO, null, 
                           "foodName LIKE ?", new String[]{searchPattern}, 
                           null, null, "foodName ASC");
        List<FoodCalo> list = new ArrayList<>();
        if (c.moveToFirst()) { 
            do { 
                list.add(cursorToFC(c)); 
            } while (c.moveToNext()); 
        }
        c.close();
        return list;
    }

    /**
     * Tìm kiếm thực phẩm không phân biệt hoa thường
     */
    public List<FoodCalo> searchByNameIgnoreCase(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String searchPattern = "%" + keyword.toLowerCase() + "%";
        Cursor c = db.query(DBHelper.TBL_FOOD_CALO, null, 
                           "LOWER(foodName) LIKE ?", new String[]{searchPattern}, 
                           null, null, "foodName ASC");
        List<FoodCalo> list = new ArrayList<>();
        if (c.moveToFirst()) { 
            do { 
                list.add(cursorToFC(c)); 
            } while (c.moveToNext()); 
        }
        c.close();
        return list;
    }

    /**
     * Tìm kiếm thực phẩm với nhiều từ khóa
     */
    public List<FoodCalo> searchByMultipleKeywords(String... keywords) {
        if (keywords == null || keywords.length == 0) {
            return new ArrayList<>();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder whereClause = new StringBuilder();
        List<String> whereArgs = new ArrayList<>();

        for (int i = 0; i < keywords.length; i++) {
            if (i > 0) {
                whereClause.append(" OR ");
            }
            whereClause.append("LOWER(foodName) LIKE ?");
            whereArgs.add("%" + keywords[i].toLowerCase() + "%");
        }

        Cursor c = db.query(DBHelper.TBL_FOOD_CALO, null, 
                           whereClause.toString(), 
                           whereArgs.toArray(new String[0]), 
                           null, null, "foodName ASC");
        List<FoodCalo> list = new ArrayList<>();
        if (c.moveToFirst()) { 
            do { 
                list.add(cursorToFC(c)); 
            } while (c.moveToNext()); 
        }
        c.close();
        return list;
    }

    /**
     * Lấy danh sách thực phẩm theo khoảng calo
     */
    public List<FoodCalo> getByCalorieRange(int minCalories, int maxCalories) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_FOOD_CALO, null, 
                           "caloPerOneHundredGrams BETWEEN ? AND ?", 
                           new String[]{String.valueOf(minCalories), String.valueOf(maxCalories)}, 
                           null, null, "caloPerOneHundredGrams ASC");
        List<FoodCalo> list = new ArrayList<>();
        if (c.moveToFirst()) { 
            do { 
                list.add(cursorToFC(c)); 
            } while (c.moveToNext()); 
        }
        c.close();
        return list;
    }

    private ContentValues toCV(FoodCalo f, boolean includeId) {
        ContentValues cv = new ContentValues();
        if (includeId) cv.put("foodCaloID", f.getFoodCaloID());
        cv.put("foodName", f.getFoodName());
        cv.put("foodThumb", f.getFoodThumb());
        cv.put("caloPerOneHundredGrams", f.getCaloPerOneHundredGrams());
        return cv;
    }

    /**
     * Debug method: In ra tất cả dữ liệu trong bảng FOOD_CALO
     */
    public void debugPrintAllData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TBL_FOOD_CALO, null);
        
        android.util.Log.d("FoodCaloDao", "=== DEBUG: FOOD_CALO TABLE DATA ===");
        android.util.Log.d("FoodCaloDao", "Total rows: " + c.getCount());
        
        if (c.moveToFirst()) {
            do {
                String foodName = c.getString(c.getColumnIndexOrThrow("foodName"));
                String foodThumb = c.getString(c.getColumnIndexOrThrow("foodThumb"));
                int calo = c.getInt(c.getColumnIndexOrThrow("caloPerOneHundredGrams"));
                long id = c.getLong(c.getColumnIndexOrThrow("foodCaloID"));
                
                android.util.Log.d("FoodCaloDao", String.format("ID: %d, Name: %s, Thumb: %s, Calo: %d", 
                    id, foodName, foodThumb, calo));
            } while (c.moveToNext());
        }
        
        c.close();
        android.util.Log.d("FoodCaloDao", "=== END DEBUG ===");
    }

    /**
     * Kiểm tra xem bảng có tồn tại và có dữ liệu không
     */
    public boolean isTableExists() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name=?", 
            new String[]{DBHelper.TBL_FOOD_CALO}
        );
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    /**
     * Đếm số lượng bản ghi trong bảng
     */
    public int getCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TBL_FOOD_CALO, null);
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }
} 
