package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.database.DBHelper;
import com.example.bepnhataapp.common.model.MealRecipe;

import java.util.ArrayList;
import java.util.List;

public class MealRecipeDao {

    private final DBHelper helper;

    public MealRecipeDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(MealRecipe mr) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_MEAL_RECIPES, null, toContentValues(mr));
    }

    public int delete(long mealTimeID, long recipeID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_MEAL_RECIPES, "mealTimeID=? AND recipeID=?", new String[]{String.valueOf(mealTimeID), String.valueOf(recipeID)});
    }

    public List<MealRecipe> getByMealTime(long mealTimeID) {
        List<MealRecipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_MEAL_RECIPES + " WHERE mealTimeID=?", new String[]{String.valueOf(mealTimeID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(MealRecipe mr) {
        ContentValues v = new ContentValues();
        v.put("mealTimeID", mr.getMealTimeID());
        v.put("recipeID", mr.getRecipeID());
        return v;
    }

    private MealRecipe fromCursor(Cursor cur) {
        return new MealRecipe(
                cur.getLong(cur.getColumnIndexOrThrow("mealTimeID")),
                cur.getLong(cur.getColumnIndexOrThrow("recipeID"))
        );
    }
} 