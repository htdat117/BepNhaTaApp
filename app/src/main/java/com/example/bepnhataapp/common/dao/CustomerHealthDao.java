package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.CustomerHealth;
import java.util.ArrayList;
import java.util.List;

public class CustomerHealthDao {
    private final DBHelper helper;
    public CustomerHealthDao(Context ctx){this.helper=new DBHelper(ctx.getApplicationContext());}

    public long insert(CustomerHealth h){
        return helper.getWritableDatabase().insert(DBHelper.TBL_CUSTOMER_HEALTH,null,toCV(h)); }

    public int update(CustomerHealth h){
        return helper.getWritableDatabase().update(DBHelper.TBL_CUSTOMER_HEALTH,toCV(h),"customerHealthID=?",new String[]{String.valueOf(h.getCustomerHealthID())}); }

    public CustomerHealth findByCustomer(long customerId){
        Cursor c=helper.getReadableDatabase().rawQuery("SELECT * FROM "+DBHelper.TBL_CUSTOMER_HEALTH+" WHERE customerID=? LIMIT 1",new String[]{String.valueOf(customerId)});
        if(c.moveToFirst()){CustomerHealth h=fromCursor(c);c.close();return h;}c.close();return null;}

    private ContentValues toCV(CustomerHealth h){
        ContentValues v=new ContentValues();
        v.put("customerID",h.getCustomerID());
        v.put("gender",h.getGender());
        v.put("age",h.getAge());
        v.put("weight",h.getWeight());
        v.put("height",h.getHeight());
        v.put("bodyType",h.getBodyType());
        v.put("allergy",h.getAllergy());
        v.put("commonGoal",h.getCommonGoal());
        v.put("targetWeight",h.getTargetWeight());
        v.put("weightChangeRate",h.getWeightChangeRate());
        v.put("physicalActivityLevel",h.getPhysicalActivityLevel());
        return v;
    }

    private CustomerHealth fromCursor(Cursor cur){
        CustomerHealth h=new CustomerHealth();
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
        return h;}
} 
