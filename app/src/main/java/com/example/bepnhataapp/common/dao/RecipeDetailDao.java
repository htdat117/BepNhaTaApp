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

    private ContentValues toCV(RecipeDetail d){ ContentValues v = new ContentValues(); v.put("RecipeID", d.getRecipeID()); v.put("Calo", d.getCalo()); v.put("Protein", d.getProtein()); v.put("Carbs", d.getCarbs()); v.put("Fat", d.getFat()); v.put("FoodTag", d.getFoodTag()); v.put("Cuisine", d.getCuisine()); v.put("CookingTimeMinutes", d.getCookingTimeMinutes()); v.put("Flavor", d.getFlavor()); v.put("Benefit", d.getBenefit()); v.put("Level", d.getLevel()); return v; }

    private RecipeDetail fromC(Cursor c){ RecipeDetail d=new RecipeDetail(); d.setRecipeID(c.getLong(c.getColumnIndexOrThrow("RecipeID"))); d.setCalo(c.getDouble(c.getColumnIndexOrThrow("Calo"))); d.setProtein(c.getDouble(c.getColumnIndexOrThrow("Protein"))); d.setCarbs(c.getDouble(c.getColumnIndexOrThrow("Carbs"))); d.setFat(c.getDouble(c.getColumnIndexOrThrow("Fat"))); d.setFoodTag(c.getString(c.getColumnIndexOrThrow("FoodTag"))); d.setCuisine(c.getString(c.getColumnIndexOrThrow("Cuisine"))); d.setCookingTimeMinutes(c.getInt(c.getColumnIndexOrThrow("CookingTimeMinutes"))); d.setFlavor(c.getString(c.getColumnIndexOrThrow("Flavor"))); d.setBenefit(c.getString(c.getColumnIndexOrThrow("Benefit"))); int idx=c.getColumnIndex("Level"); if(idx!=-1) d.setLevel(c.getString(idx)); return d; }
} 
