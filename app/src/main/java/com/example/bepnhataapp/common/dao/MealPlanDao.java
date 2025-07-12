package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.MealPlan;

import java.util.ArrayList;
import java.util.List;

public class MealPlanDao {

    private final DBHelper helper;

    public MealPlanDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(MealPlan m) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_MEAL_PLANS, null, toContentValues(m));
    }

    public int update(MealPlan m) {
        return helper.getWritableDatabase().update(DBHelper.TBL_MEAL_PLANS, toContentValues(m), "mealPlanID=?", new String[]{String.valueOf(m.getMealPlanID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_MEAL_PLANS, "mealPlanID=?", new String[]{String.valueOf(id)});
    }

    public List<MealPlan> getAll() {
        List<MealPlan> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_PLANS + " ORDER BY mealPlanID DESC", null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public boolean hasMealPlanForCustomer(long customerId) {
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT 1 FROM " + DBHelper.TBL_MEAL_PLANS + " WHERE customerID=? LIMIT 1", new String[]{String.valueOf(customerId)});
        boolean exists = cur.moveToFirst();
        cur.close();
        return exists;
    }

    public List<MealPlan> getTemplates() {
        List<MealPlan> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_MEAL_PLANS +
            " WHERE customerID IS NULL AND (type!='AUTO' OR type IS NULL)",
            null
        );
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<MealPlan> getAutoPlans() {
        List<MealPlan> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_PLANS + " WHERE type='AUTO'", null);
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public MealPlan getPersonalPlan(long customerId) {
        Cursor cur = null;
        try {
            cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_PLANS + " WHERE customerID=? AND type='PERSONAL' LIMIT 1", new String[]{String.valueOf(customerId)});
            if (cur.moveToFirst()) {
                return fromCursor(cur);
            }
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return null;
    }

    public String getRandomRecipeImageForPlan(long mealPlanId) {
        String imageThumb = null;
        String query = "SELECT R.imageThumb FROM " + DBHelper.TBL_RECIPES + " R" +
                " JOIN " + DBHelper.TBL_MEAL_RECIPES + " MR ON R.recipeID = MR.recipeID" +
                " JOIN " + DBHelper.TBL_MEAL_TIMES + " MT ON MR.mealTimeID = MT.mealTimeID" +
                " JOIN " + DBHelper.TBL_MEAL_DAYS + " MD ON MT.mealDayID = MD.mealDayID" +
                " WHERE MD.mealPlanID = ?" +
                " ORDER BY RANDOM() LIMIT 1";

        Cursor cur = helper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(mealPlanId)});
        if (cur.moveToFirst()) {
            imageThumb = cur.getString(cur.getColumnIndexOrThrow("imageThumb"));
        }
        cur.close();
        return imageThumb;
    }

    public boolean existsPersonalPlanForCustomer(long customerId){
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT 1 FROM " + DBHelper.TBL_MEAL_PLANS + " WHERE customerID=? AND type='PERSONAL' LIMIT 1",
            new String[]{String.valueOf(customerId)});
        boolean ok = cur.moveToFirst();
        cur.close();
        return ok;
    }

    private ContentValues toContentValues(MealPlan m) {
        ContentValues v = new ContentValues();
        if (m.getCustomerID() != null) v.put("customerID", m.getCustomerID()); else v.putNull("customerID");
        v.put("mealCategory", m.getMealCategory());
        v.put("title", m.getTitle());
        v.put("createdAt", m.getCreatedAt());
        v.put("imageThumb", m.getImageThumb());
        v.put("totalDays", m.getTotalDays());
        v.put("avgCalories", m.getAvgCalories());
        v.put("avgCarbs", m.getAvgCarbs());
        v.put("avgProtein", m.getAvgProtein());
        v.put("avgFat", m.getAvgFat());
        v.put("startDate", m.getStartDate());
        v.put("endDate", m.getEndDate());
        v.put("note", m.getNote());
        v.put("type", m.getType());
        return v;
    }

    private MealPlan fromCursor(Cursor cur) {
        MealPlan m = new MealPlan();
        m.setMealPlanID(cur.getLong(cur.getColumnIndexOrThrow("mealPlanID")));
        if (!cur.isNull(cur.getColumnIndexOrThrow("customerID"))) {
            m.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        }
        m.setMealCategory(cur.getString(cur.getColumnIndexOrThrow("mealCategory")));
        m.setTitle(cur.getString(cur.getColumnIndexOrThrow("title")));
        m.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        m.setImageThumb(cur.getString(cur.getColumnIndexOrThrow("imageThumb")));
        m.setTotalDays(cur.getInt(cur.getColumnIndexOrThrow("totalDays")));
        m.setAvgCalories(cur.getDouble(cur.getColumnIndexOrThrow("avgCalories")));
        m.setAvgCarbs(cur.getDouble(cur.getColumnIndexOrThrow("avgCarbs")));
        m.setAvgProtein(cur.getDouble(cur.getColumnIndexOrThrow("avgProtein")));
        m.setAvgFat(cur.getDouble(cur.getColumnIndexOrThrow("avgFat")));
        m.setStartDate(cur.getString(cur.getColumnIndexOrThrow("startDate")));
        m.setEndDate(cur.getString(cur.getColumnIndexOrThrow("endDate")));
        m.setNote(cur.getString(cur.getColumnIndexOrThrow("note")));
        m.setType(cur.getString(cur.getColumnIndexOrThrow("type")));
        return m;
    }
} 
