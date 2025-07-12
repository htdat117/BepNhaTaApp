package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.InstructionRecipe;

import java.util.ArrayList;
import java.util.List;

public class InstructionRecipeDao {

    private final DBHelper helper;

    public InstructionRecipeDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(InstructionRecipe ir) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_INSTRUCTION_RECIPES, null, toContentValues(ir));
    }

    public int update(InstructionRecipe ir) {
        return helper.getWritableDatabase().update(DBHelper.TBL_INSTRUCTION_RECIPES, toContentValues(ir), "instructionID=?", new String[]{String.valueOf(ir.getInstructionID())});
    }

    public int delete(long instructionID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_INSTRUCTION_RECIPES, "instructionID=?", new String[]{String.valueOf(instructionID)});
    }

    public InstructionRecipe get(long instructionID) {
        Cursor c = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_INSTRUCTION_RECIPES + " WHERE instructionID=?", 
            new String[]{String.valueOf(instructionID)}
        );
        InstructionRecipe ir = null;
        if (c.moveToFirst()) ir = fromCursor(c);
        c.close();
        return ir;
    }

    public List<InstructionRecipe> getAll() {
        List<InstructionRecipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM " + DBHelper.TBL_INSTRUCTION_RECIPES + " ORDER BY recipeID, numberSection", 
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

    public List<InstructionRecipe> getByRecipe(long recipeID) {
        List<InstructionRecipe> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_INSTRUCTION_RECIPES + " WHERE recipeID=? ORDER BY numberSection", new String[]{String.valueOf(recipeID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(InstructionRecipe ir) {
        ContentValues cv = new ContentValues();
        cv.put("instructionID", ir.getInstructionID());
        cv.put("recipeID", ir.getRecipeID());
        cv.put("numberSection", ir.getNumberSection());
        cv.put("title", ir.getTitle());
        cv.put("content", ir.getContent());
        cv.put("image", ir.getImage());
        return cv;
    }

    private InstructionRecipe fromCursor(Cursor cur) {
        InstructionRecipe ir = new InstructionRecipe();
        ir.setInstructionID(cur.getLong(cur.getColumnIndexOrThrow("instructionID")));
        ir.setRecipeID(cur.getLong(cur.getColumnIndexOrThrow("recipeID")));
        ir.setNumberSection(cur.getInt(cur.getColumnIndexOrThrow("numberSection")));
        ir.setTitle(cur.getString(cur.getColumnIndexOrThrow("title")));
        ir.setContent(cur.getString(cur.getColumnIndexOrThrow("content")));
        ir.setImage(cur.getString(cur.getColumnIndexOrThrow("image")));
        return ir;
    }
} 
