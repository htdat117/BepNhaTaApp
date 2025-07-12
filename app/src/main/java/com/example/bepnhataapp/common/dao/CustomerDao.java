package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    private final DBHelper helper;

    public CustomerDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(Customer c) {
        ContentValues v = toContentValues(c);
        return helper.getWritableDatabase().insert(DBHelper.TBL_CUSTOMERS, null, v);
    }

    public int update(Customer c) {
        ContentValues v = toContentValues(c);
        return helper.getWritableDatabase().update(DBHelper.TBL_CUSTOMERS, v, "customerID=?", new String[]{String.valueOf(c.getCustomerID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_CUSTOMERS, "customerID=?", new String[]{String.valueOf(id)});
    }

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        Cursor cursor = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CUSTOMERS + " ORDER BY customerID DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Customer findByPhone(String phone) {
        Cursor cursor = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CUSTOMERS + " WHERE phone=? LIMIT 1", new String[]{phone});
        if (cursor.moveToFirst()) {
            Customer c = fromCursor(cursor);
            cursor.close();
            return c;
        }
        cursor.close();
        return null;
    }

    public Customer findByPhoneAndPassword(String phone,String password){
        android.database.Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM "+DBHelper.TBL_CUSTOMERS+" WHERE phone=? AND password=? LIMIT 1", new String[]{phone,password});
        if(cur.moveToFirst()){
            Customer c = fromCursor(cur);
            cur.close();
            return c;
        }
        cur.close();
        return null;
    }

    public Customer findById(long id) {
        Cursor cursor = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CUSTOMERS + " WHERE customerID=? LIMIT 1", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Customer c = fromCursor(cursor);
            cursor.close();
            return c;
        }
        cursor.close();
        return null;
    }

    private ContentValues toContentValues(Customer c) {
        ContentValues v = new ContentValues();
        v.put("fullName", c.getFullName());
        v.put("gender", c.getGender());
        v.put("birthday", c.getBirthday());
        v.put("email", c.getEmail());
        v.put("password", c.getPassword());
        v.put("phone", c.getPhone());
        if(c.getAvatar()!=null && c.getAvatar().length>0){
            v.put("avatar", c.getAvatar());
        } else if(c.getAvatarLink()!=null){
            v.put("avatar", c.getAvatarLink());
        } else {
            v.putNull("avatar");
        }
        v.put("customerType", c.getCustomerType());
        v.put("loyaltyPoint", c.getLoyaltyPoint());
        v.put("createdAt", c.getCreatedAt());
        v.put("status", c.getStatus());
        return v;
    }

    private Customer fromCursor(Cursor cur) {
        Customer c = new Customer();
        c.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        c.setFullName(cur.getString(cur.getColumnIndexOrThrow("fullName")));
        c.setGender(cur.getString(cur.getColumnIndexOrThrow("gender")));
        c.setBirthday(cur.getString(cur.getColumnIndexOrThrow("birthday")));
        c.setEmail(cur.getString(cur.getColumnIndexOrThrow("email")));
        c.setPassword(cur.getString(cur.getColumnIndexOrThrow("password")));
        c.setPhone(cur.getString(cur.getColumnIndexOrThrow("phone")));
        int colIdx = cur.getColumnIndexOrThrow("avatar");
        int type = cur.getType(colIdx);
        if(type == android.database.Cursor.FIELD_TYPE_BLOB){
            byte[] blob = cur.getBlob(colIdx);
            if(blob!=null && blob.length>0) c.setAvatar(blob);
        } else if(type == android.database.Cursor.FIELD_TYPE_STRING){
            String avStr = cur.getString(colIdx);
            if(avStr!=null && !avStr.isEmpty()) c.setAvatarLink(avStr);
        } else {
            // fallback attempt: try blob then string
            byte[] blob = cur.getBlob(colIdx);
            if(blob!=null && blob.length>0) c.setAvatar(blob);
            else {
                String avStr = cur.getString(colIdx);
                if(avStr!=null && !avStr.isEmpty()) c.setAvatarLink(avStr);
            }
        }
        c.setCustomerType(cur.getString(cur.getColumnIndexOrThrow("customerType")));
        c.setLoyaltyPoint(cur.getInt(cur.getColumnIndexOrThrow("loyaltyPoint")));
        c.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        c.setStatus(cur.getString(cur.getColumnIndexOrThrow("status")));
        return c;
    }
} 
