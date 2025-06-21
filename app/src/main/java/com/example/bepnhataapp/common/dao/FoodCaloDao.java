package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.database.DBHelper;
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
        f.setFoodThumb(c.getBlob(c.getColumnIndexOrThrow("foodThumb")));  // blob
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

    private ContentValues toCV(FoodCalo f, boolean includeId) {
        ContentValues cv = new ContentValues();
        if (includeId) cv.put("foodCaloID", f.getFoodCaloID());
        cv.put("foodName", f.getFoodName());
        cv.put("foodThumb", f.getFoodThumb());
        cv.put("caloPerOneHundredGrams", f.getCaloPerOneHundredGrams());
        return cv;
    }
} 