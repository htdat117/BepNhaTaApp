package com.example.bepnhataapp.common.utils;

import android.content.Context;
import android.util.Log;

/**
 * Helper class để xử lý lỗi GoogleApiManager một cách toàn diện
 */
public class GoogleApiManagerHelper {
    private static final String TAG = "GoogleApiManagerHelper";

    /**
     * Xử lý lỗi GoogleApiManager một cách an toàn
     */
    public static void handleGoogleApiManagerError(String errorMessage, Throwable throwable) {
        try {
            Log.w(TAG, "GoogleApiManager error handled gracefully: " + errorMessage);
            
            // Kiểm tra loại lỗi cụ thể
            if (errorMessage != null && errorMessage.contains("Unknown calling package name")) {
                Log.w(TAG, "This is a known Google Play Services issue on some devices/emulators");
                // Không làm gì cả, đây là lỗi đã biết
            } else if (errorMessage != null && errorMessage.contains("Failed to get service from broker")) {
                Log.w(TAG, "Google Play Services broker issue detected");
                // Không làm gì cả, đây là lỗi đã biết
            } else {
                Log.w(TAG, "Unknown GoogleApiManager error: " + errorMessage);
            }
            
            if (throwable != null) {
                Log.w(TAG, "GoogleApiManager throwable: " + throwable.getMessage());
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error handling GoogleApiManager error", e);
        }
    }

    /**
     * Kiểm tra xem lỗi có phải là lỗi GoogleApiManager không
     */
    public static boolean isGoogleApiManagerError(String errorMessage) {
        if (errorMessage == null) return false;
        
        return errorMessage.contains("GoogleApiManager") ||
               errorMessage.contains("Failed to get service from broker") ||
               errorMessage.contains("Unknown calling package name") ||
               errorMessage.contains("com.google.android.gms");
    }

    /**
     * Xử lý lỗi một cách tự động
     */
    public static void handleError(String errorMessage, Throwable throwable) {
        if (isGoogleApiManagerError(errorMessage)) {
            handleGoogleApiManagerError(errorMessage, throwable);
        } else {
            Log.e(TAG, "Non-GoogleApiManager error: " + errorMessage, throwable);
        }
    }

    /**
     * Khởi tạo GoogleApiManagerHelper
     */
    public static void initialize(Context context) {
        try {
            Log.d(TAG, "GoogleApiManagerHelper initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing GoogleApiManagerHelper", e);
        }
    }
} 
