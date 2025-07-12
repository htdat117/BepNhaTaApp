package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.MealDay;

import java.util.ArrayList;
import java.util.List;

public class MealDayDao {

    private final DBHelper helper;

    public MealDayDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(MealDay d) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_MEAL_DAYS, null, toContentValues(d));
    }

    public int update(MealDay d) {
        return helper.getWritableDatabase().update(DBHelper.TBL_MEAL_DAYS, toContentValues(d), "mealDayID=?", new String[]{String.valueOf(d.getMealDayID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_MEAL_DAYS, "mealDayID=?", new String[]{String.valueOf(id)});
    }

    public List<MealDay> getAll() {
        List<MealDay> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_DAYS + " ORDER BY mealDayID DESC", null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<MealDay> getByMealPlan(long mealPlanID) {
        List<MealDay> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_DAYS + " WHERE mealPlanID=? ORDER BY date", new String[]{String.valueOf(mealPlanID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public MealDay getByDate(String date) {
        MealDay d = null;
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_DAYS + " WHERE date=?", new String[]{date});
        if (cur.moveToFirst()) {
            d = fromCursor(cur);
        }
        cur.close();
        return d;
    }

    private ContentValues toContentValues(MealDay d) {
        ContentValues v = new ContentValues();
        v.put("mealPlanID", d.getMealPlanID());
        v.put("date", d.getDate());
        v.put("note", d.getNote());
        return v;
    }

    private MealDay fromCursor(Cursor cur) {
        MealDay d = new MealDay();
        d.setMealDayID(cur.getLong(cur.getColumnIndexOrThrow("mealDayID")));
        d.setMealPlanID(cur.getLong(cur.getColumnIndexOrThrow("mealPlanID")));
        d.setDate(cur.getString(cur.getColumnIndexOrThrow("date")));
        d.setNote(cur.getString(cur.getColumnIndexOrThrow("note")));
        return d;
    }

    /**
     * Lấy tất cả bản ghi MealDay của ngày cụ thể.
     */
    public List<MealDay> getAllByDate(String date) {
        List<MealDay> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_MEAL_DAYS + " WHERE date=?", new String[]{date}
        );
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }
} 
