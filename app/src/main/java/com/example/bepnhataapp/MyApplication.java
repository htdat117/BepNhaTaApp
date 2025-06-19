package com.example.bepnhataapp;

import android.app.Application;
import android.util.Log;

import com.example.bepnhataapp.common.database.DBHelper;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize database early so schema is ready for activities/fragments
        try {
            new DBHelper(this).getReadableDatabase();
            Log.d(TAG, "Database initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database", e);
        }
    }
} 