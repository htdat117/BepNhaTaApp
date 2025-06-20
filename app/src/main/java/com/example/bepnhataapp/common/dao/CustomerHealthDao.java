package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.database.DBHelper;
import com.example.bepnhataapp.common.model.CustomerHealth;

import java.util.ArrayList;
import java.util.List;

public class CustomerHealthDao {

    private final DBHelper helper;

    public CustomerHealthDao(Context ctx) {
        helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(CustomerHealth h) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_CUSTOMER_HEALTH, null, toContentValues(h));
    }

    public int update(CustomerHealth h) {
        return helper.getWritableDatabase().update(DBHelper.TBL_CUSTOMER_HEALTH, toContentValues(h), "customerHealthID=?", new String[]{String.valueOf(h.getCustomerHealthID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_CUSTOMER_HEALTH, "customerHealthID=?", new String[]{String.valueOf(id)});
    }

    public CustomerHealth getByCustomer(long customerID) {
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CUSTOMER_HEALTH + " WHERE customerID=?", new String[]{String.valueOf(customerID)});
        CustomerHealth h = null;
        if (cur.moveToFirst()) {
            h = fromCursor(cur);
        }
        cur.close();
        return h;
    }

    private ContentValues toContentValues(CustomerHealth h) {
        ContentValues v = new ContentValues();
        v.put("customerID", h.getCustomerID());
        v.put("gender", h.getGender());
        v.put("age", h.getAge());
        v.put("weight", h.getWeight());
        v.put("height", h.getHeight());
        v.put("bodyType", h.getBodyType());
        v.put("allergy", h.getAllergy());
        v.put("commonGoal", h.getCommonGoal());
        v.put("targetWeight", h.getTargetWeight());
        v.put("weightChangeRate", h.getWeightChangeRate());
        v.put("physicalActivityLevel", h.getPhysicalActivityLevel());
        return v;
    }

    private CustomerHealth fromCursor(Cursor cur) {
        CustomerHealth h = new CustomerHealth();
        h.setCustomerHealthID(cur.getLong(cur.getColumnIndexOrThrow("customerHealthID")));
        h.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        h.setGender(cur.getString(cur.getColumnIndexOrThrow("gender")));
        h.setAge(cur.getInt(cur.getColumnIndexOrThrow("age")));
        h.setWeight(cur.getDouble(cur.getColumnIndexOrThrow("weight")));
        h.setHeight(cur.getDouble(cur.getColumnIndexOrThrow("height")));
        h.setBodyType(cur.getString(cur.getColumnIndexOrThrow("bodyType")));
        h.setAllergy(cur.getString(cur.getColumnIndexOrThrow("allergy")));
        h.setCommonGoal(cur.getString(cur.getColumnIndexOrThrow("commonGoal")));
        h.setTargetWeight(cur.getDouble(cur.getColumnIndexOrThrow("targetWeight")));
        h.setWeightChangeRate(cur.getDouble(cur.getColumnIndexOrThrow("weightChangeRate")));
        h.setPhysicalActivityLevel(cur.getString(cur.getColumnIndexOrThrow("physicalActivityLevel")));
        return h;
    }
} 