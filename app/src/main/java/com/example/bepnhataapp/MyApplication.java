package com.example.bepnhataapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.utils.GooglePlayServicesHelper;
import com.example.bepnhataapp.common.utils.GoogleApiManagerHelper;
import com.google.firebase.FirebaseApp;
import java.io.IOException;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    public static volatile boolean isDbReady = false;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        
        // Khởi tạo Firebase
        try {
            FirebaseApp.initializeApp(this);
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase", e);
        }
        
        // Khởi tạo GoogleApiManagerHelper
        GoogleApiManagerHelper.initialize(this);
        
        // Khởi tạo Google Play Services một cách an toàn và không đồng bộ
        initializeGooglePlayServicesAsync();
        
        // Khởi tạo database
        initializeDatabaseAsync();
    }

    private void initializeGooglePlayServicesAsync() {
        new Thread(() -> {
            try {
                // Delay một chút để tránh conflict
                Thread.sleep(500);
                GooglePlayServicesHelper.initializeGooglePlayServices(this);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Google Play Services", e);
            }
        }).start();
    }

    private void initializeDatabaseAsync() {
        new Thread(() -> {
            try {
                // Khởi tạo database: copy file từ assets nếu chưa tồn tại
                DBHelper dbHelper = new DBHelper(MyApplication.this);
                // Thử tạo database từ assets trước khi mở
                try {
                    dbHelper.createDatabase();
                } catch (IOException ioException) {
                    Log.e(TAG, "Error copying pre-populated database", ioException);
                }

                // Sau khi copy (hoặc nếu đã tồn tại) thì mở database để bảo đảm schema sẵn sàng
                dbHelper.getReadableDatabase();
                dbHelper.close();

                isDbReady = true;
                Log.d(TAG, "Database initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing database", e);
                isDbReady = true; // Vẫn set true để app không bị treo
            }
        }).start();
    }

    public static Context getAppContext() {
        return context;
    }
} 
