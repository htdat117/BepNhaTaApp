package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
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

    /**
     * Get all feedbacks of a product by joining order lines.
     */
    public List<ProductFeedback> getByProductId(long productID){
        List<ProductFeedback> list = new ArrayList<>();
        String sql = "SELECT pf.* FROM " + DBHelper.TBL_PRODUCT_FEEDBACK + " pf " +
                "JOIN " + DBHelper.TBL_ORDER_LINES + " ol ON pf.orderLineID = ol.orderLineID " +
                "WHERE ol.productID = ?";
        Cursor cur = helper.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(productID)});
        if(cur.moveToFirst()){
            do {
                list.add(fromCursor(cur));
            } while(cur.moveToNext());
        }
        cur.close();
        return list;
    }

    public List<com.example.bepnhataapp.common.models.Review> getReviewsByProductId(long productID){
        List<com.example.bepnhataapp.common.models.Review> reviews = new ArrayList<>();
        String sql = "SELECT pf.content, pf.rating, pf.createdAt, c.fullName, c.avatar " +
                "FROM " + DBHelper.TBL_PRODUCT_FEEDBACK + " pf " +
                "JOIN " + DBHelper.TBL_ORDER_LINES + " ol ON pf.orderLineID = ol.orderLineID " +
                "JOIN " + DBHelper.TBL_ORDERS + " o ON ol.orderID = o.orderID " +
                "JOIN " + DBHelper.TBL_CARTS + " ct ON o.cartID = ct.cartID " +
                "JOIN " + DBHelper.TBL_CUSTOMERS + " c ON ct.customerID = c.customerID " +
                "WHERE ol.productID = ?";
        Cursor cur = helper.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(productID)});
        if(cur.moveToFirst()){
            do{
                String content = cur.getString(0);
                int rating = cur.getInt(1);
                String createdAt = cur.getString(2);
                String fullName = cur.getString(3);
                String avatar = cur.getString(4);
                reviews.add(new com.example.bepnhataapp.common.models.Review(avatar, fullName, rating, createdAt, content, new ArrayList<>()));
            }while(cur.moveToNext());
        }
        cur.close();
        return reviews;
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