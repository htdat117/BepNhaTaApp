package com.example.bepnhataapp;

import android.app.Application;
import android.util.Log;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    public static volatile boolean isDbReady = false;

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);
        
        new Thread(() -> {
            DBHelper dbHelper = new DBHelper(this);
            try {
                dbHelper.createDatabase();
                Log.d(TAG, "Database created/checked successfully in background.");
                isDbReady = true; // Giơ cờ hiệu: Cơ sở dữ liệu đã sẵn sàng!
            } catch (Exception e) {
                Log.e(TAG, "Error creating database in background", e);
            }
        }).start();
    }
} 