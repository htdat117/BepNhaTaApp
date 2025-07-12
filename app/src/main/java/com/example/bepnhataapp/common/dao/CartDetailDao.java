package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.CartDetail;

import java.util.ArrayList;
import java.util.List;

public class CartDetailDao {

    private final DBHelper helper;

    public CartDetailDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(CartDetail cd) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_CART_DETAILS, null, toContentValues(cd));
    }

    public int update(CartDetail cd) {
        return helper.getWritableDatabase().update(DBHelper.TBL_CART_DETAILS, toContentValues(cd), "cartID=? AND productID=? AND servingFactor=?", new String[]{String.valueOf(cd.getCartID()), String.valueOf(cd.getProductID()), String.valueOf(cd.getServingFactor())});
    }

    public int delete(long cartID, long productID, int servingFactor) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_CART_DETAILS, "cartID=? AND productID=? AND servingFactor=?", new String[]{String.valueOf(cartID), String.valueOf(productID), String.valueOf(servingFactor)});
    }

    public List<CartDetail> getByCart(long cartID) {
        List<CartDetail> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_CART_DETAILS + " WHERE cartID=?", new String[]{String.valueOf(cartID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(CartDetail cd) {
        ContentValues v = new ContentValues();
        v.put("cartID", cd.getCartID());
        v.put("productID", cd.getProductID());
        v.put("servingFactor", cd.getServingFactor());
        v.put("quantity", cd.getQuantity());
        return v;
    }

    private CartDetail fromCursor(Cursor cur) {
        return new CartDetail(
                cur.getLong(cur.getColumnIndexOrThrow("cartID")),
                cur.getLong(cur.getColumnIndexOrThrow("productID")),
                cur.getInt(cur.getColumnIndexOrThrow("servingFactor")),
                cur.getInt(cur.getColumnIndexOrThrow("quantity"))
        );
    }
} 
