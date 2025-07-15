package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.BlogEntity;

import java.util.ArrayList;
import java.util.List;

public class BlogDao {

    private final DBHelper helper;

    public BlogDao(Context ctx) { this.helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(BlogEntity b) { return helper.getWritableDatabase().insert(DBHelper.TBL_BLOGS, null, toContentValues(b)); }

    public int update(BlogEntity b) {
        return helper.getWritableDatabase().update(DBHelper.TBL_BLOGS, toContentValues(b), "blogID=?", new String[]{String.valueOf(b.getBlogID())});
    }

    public int delete(long id) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_BLOGS, "blogID=?", new String[]{String.valueOf(id)});
    }

    public BlogEntity get(long id) {
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_BLOGS + " WHERE blogID=?", new String[]{String.valueOf(id)});
        BlogEntity b = null;
        if (c.moveToFirst()) b = fromCursor(c);
        c.close();
        return b;
    }

    public List<BlogEntity> getAll() {
        List<BlogEntity> list = new ArrayList<>();
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_BLOGS + " ORDER BY createdAt DESC", null);
        if (c.moveToFirst()) {
            do { list.add(fromCursor(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<BlogEntity> getByStatus(String status) {
        List<BlogEntity> list = new ArrayList<>();
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_BLOGS + " WHERE status=? ORDER BY createdAt DESC", new String[]{status});
        if (c.moveToFirst()) {
            do { list.add(fromCursor(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<BlogEntity> getRandomBlogs(int limit) {
        List<BlogEntity> list = new ArrayList<>();
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_BLOGS + " ORDER BY RANDOM() LIMIT ?", new String[]{String.valueOf(limit)});
        if (c.moveToFirst()) {
            do {
                list.add(fromCursor(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private ContentValues toContentValues(BlogEntity b) {
        ContentValues v = new ContentValues();
        if (b.getBlogID() > 0) v.put("blogID", b.getBlogID());
        v.put("title", b.getTitle());
        v.put("content", b.getContent());
        v.put("authorName", b.getAuthorName());
        v.put("createdAt", b.getCreatedAt());
        if (b.getImageThumb() != null) v.put("imageThumb", b.getImageThumb()); else v.putNull("imageThumb");
        v.put("status", b.getStatus());
        v.put("tag", b.getTag());
        v.put("likes", b.getLikes());
        return v;
    }

    private BlogEntity fromCursor(Cursor cur) {
        BlogEntity b = new BlogEntity();
        b.setBlogID(cur.getLong(cur.getColumnIndexOrThrow("blogID")));
        b.setTitle(cur.getString(cur.getColumnIndexOrThrow("title")));
        b.setContent(cur.getString(cur.getColumnIndexOrThrow("content")));
        b.setAuthorName(cur.getString(cur.getColumnIndexOrThrow("authorName")));
        b.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        b.setImageThumb(cur.getString(cur.getColumnIndexOrThrow("imageThumb")));
        b.setStatus(cur.getString(cur.getColumnIndexOrThrow("status")));
        b.setTag(cur.getString(cur.getColumnIndexOrThrow("tag")));
        int likesCol = cur.getColumnIndex("likes");
        if (likesCol >= 0) b.setLikes(cur.getInt(likesCol));
        return b;
    }
} 
