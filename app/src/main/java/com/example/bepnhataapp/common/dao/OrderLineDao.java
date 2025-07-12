package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.OrderLine;

import java.util.ArrayList;
import java.util.List;

public class OrderLineDao {

    private final DBHelper helper;

    public OrderLineDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(OrderLine ol) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_ORDER_LINES, null, toContentValues(ol));
    }

    public int update(OrderLine ol) {
        return helper.getWritableDatabase().update(DBHelper.TBL_ORDER_LINES, toContentValues(ol),
                "orderLineID=?", new String[]{String.valueOf(ol.getOrderLineID())});
    }

    public int delete(long orderLineID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_ORDER_LINES, "orderLineID=?",
                new String[]{String.valueOf(orderLineID)});
    }

    public List<OrderLine> getByOrder(long orderID) {
        List<OrderLine> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_ORDER_LINES + " WHERE orderID=?",
                new String[]{String.valueOf(orderID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(OrderLine ol) {
        ContentValues cv = new ContentValues();
        cv.put("orderID", ol.getOrderID());
        cv.put("productID", ol.getProductID());
        cv.put("quantity", ol.getQuantity());
        cv.put("totalPrice", ol.getTotalPrice());
        return cv;
    }

    private OrderLine fromCursor(Cursor cur) {
        OrderLine ol = new OrderLine();
        ol.setOrderLineID(cur.getLong(cur.getColumnIndexOrThrow("orderLineID")));
        ol.setOrderID(cur.getLong(cur.getColumnIndexOrThrow("orderID")));
        ol.setProductID(cur.getLong(cur.getColumnIndexOrThrow("productID")));
        ol.setQuantity(cur.getInt(cur.getColumnIndexOrThrow("quantity")));
        ol.setTotalPrice(cur.getDouble(cur.getColumnIndexOrThrow("totalPrice")));
        return ol;
    }
} 
