package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Coupon;

import java.util.ArrayList;
import java.util.List;

public class CouponDao {

    private final DBHelper helper;

    public CouponDao(Context ctx) { this.helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(Coupon c) { return helper.getWritableDatabase().insert(DBHelper.TBL_COUPONS, null, toCV(c)); }

    public int update(Coupon c) {
        return helper.getWritableDatabase().update(DBHelper.TBL_COUPONS, toCV(c), "couponID=?", new String[]{String.valueOf(c.getCouponID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_COUPONS, "couponID=?", new String[]{String.valueOf(id)});
    }

    public Coupon get(long id) {
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_COUPONS + " WHERE couponID=?", new String[]{String.valueOf(id)});
        Coupon c = null;
        if (cur.moveToFirst()) c = fromCursor(cur);
        cur.close();
        return c;
    }

    public List<Coupon> getAvailableForCustomer(long customerID, int orderPrice) {
        List<Coupon> list = new ArrayList<>();
        String sql;
        String[] args;
        if (orderPrice <= 0) {
            // Lấy tất cả coupon bất kể minPrice
            sql = "SELECT * FROM " + DBHelper.TBL_COUPONS + " WHERE (isGeneral=1 OR customerID=? OR customerID IS NULL)";
            args = new String[]{String.valueOf(customerID)};
        } else {
            sql = "SELECT * FROM " + DBHelper.TBL_COUPONS + " WHERE (isGeneral=1 OR customerID=? OR customerID IS NULL) AND minPrice<=?";
            args = new String[]{String.valueOf(customerID), String.valueOf(orderPrice)};
        }
        Cursor cur = helper.getReadableDatabase().rawQuery(sql, args);
        if (cur.moveToFirst()) {
            do { list.add(fromCursor(cur)); } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toCV(Coupon c) {
        ContentValues v = new ContentValues();
        if (c.getCouponID() > 0) v.put("couponID", c.getCouponID());
        if (c.getCustomerID() != null) v.put("customerID", c.getCustomerID()); else v.putNull("customerID");
        v.put("couponTitle", c.getCouponTitle());
        v.put("minPrice", c.getMinPrice());
        v.put("validDate", c.getValidDate());
        v.put("expireDate", c.getExpireDate());
        if (c.getMaxDiscount() != null) v.put("maxDiscount", c.getMaxDiscount()); else v.putNull("maxDiscount");
        v.put("couponValue", c.getCouponValue());
        v.put("isGeneral", c.getIsGeneral());
        if (c.getExchangePoints() != null) v.put("exchangePoints", c.getExchangePoints()); else v.putNull("exchangePoints");
        return v;
    }

    private Coupon fromCursor(Cursor cur) {
        Coupon c = new Coupon();
        c.setCouponID(cur.getLong(cur.getColumnIndexOrThrow("couponID")));
        if (!cur.isNull(cur.getColumnIndexOrThrow("customerID"))) c.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        c.setCouponTitle(cur.getString(cur.getColumnIndexOrThrow("couponTitle")));
        c.setMinPrice(cur.getInt(cur.getColumnIndexOrThrow("minPrice")));
        c.setValidDate(cur.getString(cur.getColumnIndexOrThrow("validDate")));
        c.setExpireDate(cur.getString(cur.getColumnIndexOrThrow("expireDate")));
        if (!cur.isNull(cur.getColumnIndexOrThrow("maxDiscount"))) c.setMaxDiscount(cur.getInt(cur.getColumnIndexOrThrow("maxDiscount")));
        c.setCouponValue(cur.getInt(cur.getColumnIndexOrThrow("couponValue")));
        c.setIsGeneral(cur.getInt(cur.getColumnIndexOrThrow("isGeneral")));
        if (!cur.isNull(cur.getColumnIndexOrThrow("exchangePoints"))) c.setExchangePoints(cur.getInt(cur.getColumnIndexOrThrow("exchangePoints")));
        return c;
    }
} 
