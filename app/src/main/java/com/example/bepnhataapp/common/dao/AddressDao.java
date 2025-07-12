package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressDao {

    private final DBHelper helper;

    public AddressDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    // CRUD operations
    public long insert(Address a) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_ADDRESSES, null, toContentValues(a));
    }

    public int update(Address a) {
        return helper.getWritableDatabase().update(DBHelper.TBL_ADDRESSES, toContentValues(a),
                "addressID=?", new String[]{String.valueOf(a.getAddressID())});
    }

    public int delete(long addressID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_ADDRESSES, "addressID=?",
                new String[]{String.valueOf(addressID)});
    }

    public Address get(long addressID) {
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_ADDRESSES + " WHERE addressID=?",
                new String[]{String.valueOf(addressID)});
        Address a = null;
        if (cur.moveToFirst()) a = fromCursor(cur);
        cur.close();
        return a;
    }

    public List<Address> getByCustomer(long customerID) {
        List<Address> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_ADDRESSES + " WHERE customerID=? ORDER BY isDefault DESC, addressID DESC",
                new String[]{String.valueOf(customerID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public Address getDefault(long customerID) {
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_ADDRESSES + " WHERE customerID=? AND isDefault=1 LIMIT 1",
                new String[]{String.valueOf(customerID)});
        Address a = null;
        if (cur.moveToFirst()) a = fromCursor(cur);
        cur.close();
        return a;
    }

    private ContentValues toContentValues(Address a) {
        ContentValues cv = new ContentValues();
        cv.put("customerID", a.getCustomerID());
        cv.put("receiverName", a.getReceiverName());
        cv.put("phone", a.getPhone());
        cv.put("addressLine", a.getAddressLine());
        cv.put("district", a.getDistrict());
        cv.put("province", a.getProvince());
        cv.put("isDefault", a.isDefault() ? 1 : 0);
        cv.put("note", a.getNote());
        return cv;
    }

    private Address fromCursor(Cursor cur) {
        Address a = new Address();
        a.setAddressID(cur.getLong(cur.getColumnIndexOrThrow("addressID")));
        a.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        a.setReceiverName(cur.getString(cur.getColumnIndexOrThrow("receiverName")));
        a.setPhone(cur.getString(cur.getColumnIndexOrThrow("phone")));
        a.setAddressLine(cur.getString(cur.getColumnIndexOrThrow("addressLine")));
        a.setDistrict(cur.getString(cur.getColumnIndexOrThrow("district")));
        a.setProvince(cur.getString(cur.getColumnIndexOrThrow("province")));
        a.setDefault(cur.getInt(cur.getColumnIndexOrThrow("isDefault")) == 1);
        a.setNote(cur.getString(cur.getColumnIndexOrThrow("note")));
        return a;
    }
} 
