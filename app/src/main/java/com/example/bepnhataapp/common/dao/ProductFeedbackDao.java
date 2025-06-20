package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.database.DBHelper;
import com.example.bepnhataapp.common.model.ProductFeedback;

import java.util.ArrayList;
import java.util.List;

public class ProductFeedbackDao {

    private final DBHelper helper;

    public ProductFeedbackDao(Context ctx) {
        this.helper = new DBHelper(ctx.getApplicationContext());
    }

    public long insert(ProductFeedback pf) {
        return helper.getWritableDatabase().insert(DBHelper.TBL_PRODUCT_FEEDBACK, null, toContentValues(pf));
    }

    public int update(ProductFeedback pf) {
        return helper.getWritableDatabase().update(DBHelper.TBL_PRODUCT_FEEDBACK, toContentValues(pf),
                "proFeedbackID=?", new String[]{String.valueOf(pf.getProFeedbackID())});
    }

    public int delete(long proFeedbackID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_PRODUCT_FEEDBACK, "proFeedbackID=?",
                new String[]{String.valueOf(proFeedbackID)});
    }

    public List<ProductFeedback> getByOrderLine(long orderLineID) {
        List<ProductFeedback> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DBHelper.TBL_PRODUCT_FEEDBACK + " WHERE orderLineID=?",
                new String[]{String.valueOf(orderLineID)});
        if (cur.moveToFirst()) {
            do {
                list.add(fromCursor(cur));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toContentValues(ProductFeedback pf) {
        ContentValues cv = new ContentValues();
        cv.put("orderLineID", pf.getOrderLineID());
        cv.put("content", pf.getContent());
        cv.put("image", pf.getImage());
        cv.put("rating", pf.getRating());
        cv.put("createdAt", pf.getCreatedAt());
        return cv;
    }

    private ProductFeedback fromCursor(Cursor cur) {
        ProductFeedback pf = new ProductFeedback();
        pf.setProFeedbackID(cur.getLong(cur.getColumnIndexOrThrow("proFeedbackID")));
        pf.setOrderLineID(cur.getLong(cur.getColumnIndexOrThrow("orderLineID")));
        pf.setContent(cur.getString(cur.getColumnIndexOrThrow("content")));
        pf.setImage(cur.getString(cur.getColumnIndexOrThrow("image")));
        pf.setRating(cur.getInt(cur.getColumnIndexOrThrow("rating")));
        pf.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        return pf;
    }
} 