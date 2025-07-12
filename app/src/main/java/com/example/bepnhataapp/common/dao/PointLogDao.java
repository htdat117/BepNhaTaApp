package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.PointLog;
import java.util.ArrayList;
import java.util.List;

public class PointLogDao {
    private final DBHelper helper;
    public PointLogDao(Context ctx) { this.helper = new DBHelper(ctx.getApplicationContext()); }

    public long insert(PointLog log) {
        return helper.getWritableDatabase().insert("PointLog", null, toContentValues(log));
    }
    public List<PointLog> getByCustomer(long customerId) {
        List<PointLog> list = new ArrayList<>();
        Cursor cur = helper.getReadableDatabase().rawQuery(
            "SELECT * FROM PointLog WHERE customerId=? ORDER BY createdAt DESC",
            new String[]{String.valueOf(customerId)});
        if (cur.moveToFirst()) {
            do { list.add(fromCursor(cur)); } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }
    private ContentValues toContentValues(PointLog log) {
        ContentValues cv = new ContentValues();
        cv.put("customerId", log.getCustomerId());
        cv.put("action", log.getAction());
        cv.put("point", log.getPoint());
        cv.put("description", log.getDescription());
        cv.put("createdAt", log.getCreatedAt());
        return cv;
    }
    private PointLog fromCursor(Cursor cur) {
        PointLog log = new PointLog();
        log.setId(cur.getLong(cur.getColumnIndexOrThrow("id")));
        log.setCustomerId(cur.getLong(cur.getColumnIndexOrThrow("customerId")));
        log.setAction(cur.getString(cur.getColumnIndexOrThrow("action")));
        log.setPoint(cur.getInt(cur.getColumnIndexOrThrow("point")));
        log.setDescription(cur.getString(cur.getColumnIndexOrThrow("description")));
        log.setCreatedAt(cur.getString(cur.getColumnIndexOrThrow("createdAt")));
        return log;
    }
} 
