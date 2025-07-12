package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.MealRecipe;

import java.util.ArrayList;
import java.util.List;

public class MealRecipeDao {

    private final DBHelper helper;

    public MealRecipeDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    /**
     * Holder for aggregated macro-nutrient totals of all recipes belonging to a given mealTime.
     */
    public static class MacroTotal {
        public final double calories;
        public final double carbs;
        public final double protein;
        public final double fat;

        public MacroTotal(double calories, double carbs, double protein, double fat) {
            this.calories = calories;
            this.carbs = carbs;
            this.protein = protein;
            this.fat = fat;
        }
    }

    /**
     * Quickly computes the sum of kcal, carbs, protein, fat for the specified {@code mealTimeID}
     * by joining MEAL_RECIPES with RECIPE_DETAILS directly in SQL (avoids iterating in Java).
     */
    public MacroTotal sumMacrosForMealTime(long mealTimeID) {
        String sql = "SELECT IFNULL(SUM(d.calo), 0)  AS calo, " +
                     "       IFNULL(SUM(d.carbs), 0) AS carbs, " +
                     "       IFNULL(SUM(d.protein), 0) AS protein, " +
                     "       IFNULL(SUM(d.fat), 0)   AS fat " +
                     "FROM " + DBHelper.TBL_MEAL_RECIPES + " mr " +
                     "JOIN " + DBHelper.TBL_RECIPE_DETAILS + " d ON mr.recipeID = d.recipeID " +
                     "WHERE mr.mealTimeID = ?";

        android.database.Cursor c = null;
        MacroTotal res = new MacroTotal(0, 0, 0, 0);
        try {
            c = helper.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(mealTimeID)});
            if (c.moveToFirst()) {
                res = new MacroTotal(
                        c.getDouble(c.getColumnIndexOrThrow("calo")),
                        c.getDouble(c.getColumnIndexOrThrow("carbs")),
                        c.getDouble(c.getColumnIndexOrThrow("protein")),
                        c.getDouble(c.getColumnIndexOrThrow("fat"))
                );
            }
        } finally {
            if (c != null) c.close();
        }
        return res;
    }

    public void insert(MealRecipe mealRecipe) {
        ContentValues values = new ContentValues();
        values.put("mealTimeID", mealRecipe.getMealTimeID());
        values.put("recipeID", mealRecipe.getRecipeID());
        helper.getWritableDatabase().insert(DBHelper.TBL_MEAL_RECIPES, null, values);
    }

    public List<MealRecipe> getRecipesForMealTime(long mealTimeID) {
        List<MealRecipe> list = new ArrayList<>();
        Cursor c = null;
        try {
            c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_RECIPES + " WHERE mealTimeID=?", new String[]{String.valueOf(mealTimeID)});
            if (c.moveToFirst()) {
                do {
                    list.add(new MealRecipe(
                        c.getLong(c.getColumnIndexOrThrow("mealTimeID")),
                        c.getLong(c.getColumnIndexOrThrow("recipeID"))
                    ));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    public void deleteByMealTime(long mealTimeID) {
        helper.getWritableDatabase().delete(DBHelper.TBL_MEAL_RECIPES, "mealTimeID=?", new String[]{String.valueOf(mealTimeID)});
    }
} 
