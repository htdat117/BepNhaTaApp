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