package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private final DBHelper helper;

    public OrderDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(Order o) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_ORDERS, null, toContentValues(o));
    }

    public int update(Order o) {
        return helper.getWritableDatabase().update(DBHelper.TBL_ORDERS, toContentValues(o),
                "orderID=?", new String[]{String.valueOf(o.getOrderID())});
    }

    public int delete(long orderID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_ORDERS, "orderID=?",
                new String[]{String.valueOf(orderID)});
    }

    public Order get(long orderID) {
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_ORDERS + " WHERE orderID=?",
                new String[]{String.valueOf(orderID)});
        Order o = null;
        if (cur.moveToFirst()) o = fromCursor(cur);
        cur.close();
        return o;
    }

    public List<Order> getByCustomer(long customerID) {
        List<Order> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT o.* FROM " + DBHelper.TBL_ORDERS + " o JOIN " + DBHelper.TBL_CARTS + " c ON o.cartID=c.cartID " +
                        "WHERE c.customerID=? ORDER BY o.orderDate DESC",
                new String[]{String.valueOf(customerID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<Order> getByCart(long cartID) {
        List<Order> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_ORDERS + " WHERE cartID=?",
                new String[]{String.valueOf(cartID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(Order o) {
        ContentValues cv = new ContentValues();
        cv.put("cartID", o.getCartID());
        cv.put("addressID", o.getAddressID());
        if (o.getCouponID() != null) cv.put("couponID", o.getCouponID()); else cv.putNull("couponID");
        cv.put("orderDate", o.getOrderDate());
        cv.put("totalPrice", o.getTotalPrice());
        cv.put("status", o.getStatus());
        cv.put("paymentMethod", o.getPaymentMethod());
        cv.put("note", o.getNote());
        return cv;
    }

    private Order fromCursor(Cursor cur) {
        Order o = new Order();
        o.setOrderID(cur.getLong(cur.getColumnIndexOrThrow("orderID")));
        o.setCartID(cur.getLong(cur.getColumnIndexOrThrow("cartID")));
        o.setAddressID(cur.getLong(cur.getColumnIndexOrThrow("addressID")));
        if (!cur.isNull(cur.getColumnIndexOrThrow("couponID"))) {
            o.setCouponID(cur.getLong(cur.getColumnIndexOrThrow("couponID")));
        }
        o.setOrderDate(cur.getString(cur.getColumnIndexOrThrow("orderDate")));
        o.setTotalPrice(cur.getDouble(cur.getColumnIndexOrThrow("totalPrice")));
        o.setStatus(cur.getString(cur.getColumnIndexOrThrow("status")));
        o.setPaymentMethod(cur.getString(cur.getColumnIndexOrThrow("paymentMethod")));
        o.setNote(cur.getString(cur.getColumnIndexOrThrow("note")));
        return o;
    }
} 
