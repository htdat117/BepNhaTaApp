package com.example.bepnhataapp.common.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GooglePlayServicesHelper {
    private static final String TAG = "GooglePlayServicesHelper";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Kiểm tra xem Google Play Services có khả dụng không
     */
    public static boolean isGooglePlayServicesAvailable(Context context) {
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
            
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    Log.w(TAG, "Google Play Services is not available: " + resultCode);
                    return false;
                } else {
                    Log.w(TAG, "This device is not supported by Google Play Services");
                    return false;
                }
            }
            
            Log.d(TAG, "Google Play Services is available");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error checking Google Play Services availability", e);
            return false;
        }
    }

    /**
     * Khởi tạo Google Play Services một cách an toàn
     */
    public static void initializeGooglePlayServices(Context context) {
        try {
            // Chỉ khởi tạo nếu thực sự cần thiết
            if (isGooglePlayServicesAvailable(context)) {
                // Google Play Services đã sẵn sàng
                Log.d(TAG, "Google Play Services initialized successfully");
            } else {
                Log.w(TAG, "Google Play Services not available, continuing without it");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Google Play Services", e);
        }
    }

    /**
     * Xử lý lỗi Google Play Services một cách an toàn
     */
    public static void handleGooglePlayServicesError(Context context, int errorCode) {
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            if (apiAvailability.isUserResolvableError(errorCode)) {
                Log.w(TAG, "Google Play Services error: " + errorCode);
            } else {
                Log.e(TAG, "Unresolvable Google Play Services error: " + errorCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling Google Play Services error", e);
        }
    }

    /**
     * Kiểm tra xem có nên sử dụng Google Play Services không
     */
    public static boolean shouldUseGooglePlayServices(Context context) {
        try {
            return isGooglePlayServicesAvailable(context);
        } catch (Exception e) {
            Log.e(TAG, "Error checking if should use Google Play Services", e);
            return false;
        }
    }

    /**
     * Xử lý lỗi GoogleApiManager một cách an toàn
     */
    public static void handleGoogleApiManagerError(String errorMessage) {
        try {
            Log.w(TAG, "GoogleApiManager error handled gracefully: " + errorMessage);
            // Không làm gì cả, chỉ log để debug
        } catch (Exception e) {
            Log.e(TAG, "Error handling GoogleApiManager error", e);
        }
    }
} 
