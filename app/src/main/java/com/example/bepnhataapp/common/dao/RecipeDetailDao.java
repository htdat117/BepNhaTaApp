package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.RecipeDetail;

public class RecipeDetailDao {
    private final DBHelper helper;

    public RecipeDetailDao(Context ctx){ helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(RecipeDetail d){ return helper.getWritableDatabase().insert(DBHelper.TBL_RECIPE_DETAILS, null, toCV(d)); }

    public int update(RecipeDetail d){ return helper.getWritableDatabase().update(DBHelper.TBL_RECIPE_DETAILS, toCV(d), "recipeID=?", new String[]{String.valueOf(d.getRecipeID())}); }

    public int delete(long recipeID){ return helper.getWritableDatabase().delete(DBHelper.TBL_RECIPE_DETAILS, "recipeID=?", new String[]{String.valueOf(recipeID)}); }

    public RecipeDetail get(long id){ Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_RECIPE_DETAILS + " WHERE recipeID=?", new String[]{String.valueOf(id)}); RecipeDetail d=null; if(c.moveToFirst()){ d = fromC(c);} c.close(); return d; }

    private ContentValues toCV(RecipeDetail d){ ContentValues v = new ContentValues(); v.put("recipeID", d.getRecipeID()); v.put("calo", d.getCalo()); v.put("protein", d.getProtein()); v.put("carbs", d.getCarbs()); v.put("fat", d.getFat()); v.put("foodTag", d.getFoodTag()); v.put("cuisine", d.getCuisine()); v.put("cookingTimeMinutes", d.getCookingTimeMinutes()); v.put("flavor", d.getFlavor()); v.put("benefit", d.getBenefit()); return v; }

    private RecipeDetail fromC(Cursor c){ RecipeDetail d=new RecipeDetail(); d.setRecipeID(c.getLong(c.getColumnIndexOrThrow("recipeID"))); d.setCalo(c.getDouble(c.getColumnIndexOrThrow("calo"))); d.setProtein(c.getDouble(c.getColumnIndexOrThrow("protein"))); d.setCarbs(c.getDouble(c.getColumnIndexOrThrow("carbs"))); d.setFat(c.getDouble(c.getColumnIndexOrThrow("fat"))); d.setFoodTag(c.getString(c.getColumnIndexOrThrow("foodTag"))); d.setCuisine(c.getString(c.getColumnIndexOrThrow("cuisine"))); d.setCookingTimeMinutes(c.getInt(c.getColumnIndexOrThrow("cookingTimeMinutes"))); d.setFlavor(c.getString(c.getColumnIndexOrThrow("flavor"))); d.setBenefit(c.getString(c.getColumnIndexOrThrow("benefit"))); return d; }
} 