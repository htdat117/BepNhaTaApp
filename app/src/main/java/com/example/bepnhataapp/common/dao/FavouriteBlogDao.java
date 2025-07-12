package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.FavouriteBlog;

import java.util.ArrayList;
import java.util.List;

public class FavouriteBlogDao {
    private final DBHelper helper;

    public FavouriteBlogDao(Context ctx) { this.helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(FavouriteBlog f) { return helper.getWritableDatabase().insert(DBHelper.TBL_FAVOURITE_BLOGS, null, toCV(f)); }

    public int delete(long blogID, long customerID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_FAVOURITE_BLOGS, "blogID=? AND customerID=?", new String[]{String.valueOf(blogID), String.valueOf(customerID)});
    }

    public boolean isFavourite(long blogID, long customerID) {
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT 1 FROM " + DBHelper.TBL_FAVOURITE_BLOGS + " WHERE blogID=? AND customerID=? LIMIT 1", new String[]{String.valueOf(blogID), String.valueOf(customerID)});
        boolean exists = c.moveToFirst(); c.close();
        return exists;
    }

    public List<FavouriteBlog> getByCustomer(long customerID) {
        List<FavouriteBlog> list = new ArrayList<>();
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_FAVOURITE_BLOGS + " WHERE customerID=?", new String[]{String.valueOf(customerID)});
        if (c.moveToFirst()) {
            do { list.add(fromCursor(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private ContentValues toCV(FavouriteBlog f) {
        ContentValues v = new ContentValues();
        v.put("blogID", f.getBlogID());
        v.put("customerID", f.getCustomerID());
        v.put("createdAt", f.getCreatedAt());
        return v;
    }

    private FavouriteBlog fromCursor(Cursor cur) {
        FavouriteBlog f = new FavouriteBlog();
        f.setBlogID(cur.getLong(cur.getColumnIndexOrThrow("blogID")));
        f.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        f.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        return f;
    }
} 
