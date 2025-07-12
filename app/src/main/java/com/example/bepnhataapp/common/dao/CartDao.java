package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDao {

    private final DBHelper helper;

    public CartDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    // Insert new cart
    public long insert(Cart c) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_CARTS, null, toContentValues(c));
    }

    // Update cart (currently only createdAt can change)
    public int update(Cart c) {
        return helper.getWritableDatabase().update(DBHelper.TBL_CARTS, toContentValues(c), "cartID=?", new String[]{String.valueOf(c.getCartID())});
    }

    public int delete(long cartID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_CARTS, "cartID=?", new String[]{String.valueOf(cartID)});
    }

    public Cart get(long cartID) {
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CARTS + " WHERE cartID=?", new String[]{String.valueOf(cartID)});
        Cart c = null;
        if (cur.moveToFirst()) c = fromCursor(cur);
        cur.close();
        return c;
    }

    public List<Cart> getByCustomer(long customerID) {
        List<Cart> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CARTS + " WHERE customerID=? ORDER BY cartID DESC", new String[]{String.valueOf(customerID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(Cart c) {
        ContentValues v = new ContentValues();
        v.put("customerID", c.getCustomerID());
        v.put("createdAt", c.getCreatedAt());
        return v;
    }

    private Cart fromCursor(Cursor cur) {
        Cart c = new Cart();
        c.setCartID(cur.getLong(cur.getColumnIndexOrThrow("cartID")));
        c.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        c.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        return c;
    }
} 
