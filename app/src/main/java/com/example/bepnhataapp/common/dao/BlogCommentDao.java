package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.BlogComment;

import java.util.ArrayList;
import java.util.List;

public class BlogCommentDao {

    private final DBHelper helper;

    public BlogCommentDao(Context ctx) { this.helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(BlogComment c) { return helper.getWritableDatabase().insert(DBHelper.TBL_BLOG_COMMENTS, null, toCV(c)); }

    public int update(BlogComment c) {
        return helper.getWritableDatabase().update(DBHelper.TBL_BLOG_COMMENTS, toCV(c), "blogCommentID=?", new String[]{String.valueOf(c.getBlogCommentID())});
    }

    public int delete(long commentID) {
        return helper.getWritableDatabase().delete(DBHelper.TBL_BLOG_COMMENTS, "blogCommentID=?", new String[]{String.valueOf(commentID)});
    }

    public BlogComment get(long commentID) {
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_BLOG_COMMENTS + " WHERE blogCommentID=?", new String[]{String.valueOf(commentID)});
        BlogComment c = null;
        if (cur.moveToFirst()) c = fromCursor(cur);
        cur.close();
        return c;
    }

    public List<BlogComment> getByBlog(long blogID) {
        List<BlogComment> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TBL_BLOG_COMMENTS + " WHERE blogID=? ORDER BY createdAt ASC", new String[]{String.valueOf(blogID)});
        if (cur.moveToFirst()) {
            do { list.add(fromCursor(cur)); } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    private ContentValues toCV(BlogComment c) {
        ContentValues v = new ContentValues();
        if (c.getBlogCommentID() > 0) v.put("blogCommentID", c.getBlogCommentID());
        v.put("blogID", c.getBlogID());
        v.put("customerID", c.getCustomerID());
        v.put("content", c.getContent());
        v.put("createdAt", c.getCreatedAt());
        if (c.getParentCommentID() != null) v.put("parentCommentID", c.getParentCommentID()); else v.putNull("parentCommentID");
        v.put("usefulness", c.getUsefulness());
        return v;
    }

    private BlogComment fromCursor(Cursor cur) {
        BlogComment c = new BlogComment();
        c.setBlogCommentID(cur.getLong(cur.getColumnIndexOrThrow("blogCommentID")));
        c.setBlogID(cur.getLong(cur.getColumnIndexOrThrow("blogID")));
        c.setCustomerID(cur.getLong(cur.getColumnIndexOrThrow("customerID")));
        c.setContent(cur.getString(cur.getColumnIndexOrThrow("content")));
        c.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        long parentVal = cur.getLong(cur.getColumnIndexOrThrow("parentCommentID"));
        if (!cur.isNull(cur.getColumnIndexOrThrow("parentCommentID"))) c.setParentCommentID(parentVal); else c.setParentCommentID(null);
        c.setUsefulness(cur.getInt(cur.getColumnIndexOrThrow("usefulness")));
        return c;
    }
} 
