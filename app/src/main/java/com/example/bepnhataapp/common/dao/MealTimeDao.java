package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.MealTime;

import java.util.ArrayList;
import java.util.List;

public class MealTimeDao {

    private final DBHelper helper;

    public MealTimeDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(MealTime t) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_MEAL_TIMES, null, toContentValues(t));
    }

    public int update(MealTime t) {
        return helper.getWritableDatabase().update(DBHelper.TBL_MEAL_TIMES, toContentValues(t), "mealTimeID=?", new String[]{String.valueOf(t.getMealTimeID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_MEAL_TIMES, "mealTimeID=?", new String[]{String.valueOf(id)});
    }

    public List<MealTime> getAll() {
        List<MealTime> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_TIMES + " ORDER BY mealTimeID DESC", null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<MealTime> getByMealDay(long mealDayID) {
        List<MealTime> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_TIMES + " WHERE mealDayID=? ORDER BY mealType", new String[]{String.valueOf(mealDayID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(MealTime t) {
        ContentValues v = new ContentValues();
        v.put("mealDayID", t.getMealDayID());
        v.put("mealType", t.getMealType());
        v.put("note", t.getNote());
        return v;
    }

    private MealTime fromCursor(Cursor cur) {
        MealTime t = new MealTime();
        t.setMealTimeID(cur.getLong(cur.getColumnIndexOrThrow("mealTimeID")));
        t.setMealDayID(cur.getLong(cur.getColumnIndexOrThrow("mealDayID")));
        t.setMealType(cur.getString(cur.getColumnIndexOrThrow("mealType")));
        t.setNote(cur.getString(cur.getColumnIndexOrThrow("note")));
        return t;
    }
} 
