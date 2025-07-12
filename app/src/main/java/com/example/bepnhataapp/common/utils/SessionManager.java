package com.example.bepnhataapp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Lightweight helper to persist simple login session info.
 * Currently only keeps track of whether user is logged-in and the phone number that
 * was used when logging in (for displaying in profile, etc.).
 *
 * NOTE: For production one should use secure storage + proper tokens.
 */
public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_PHONE = "phone";

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Mark the user as logged-in with given phone number.
     */
    public static void login(Context ctx, String phone) {
        prefs(ctx).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_PHONE, phone)
                .apply();
    }

    /**
     * Clear current session.
     */
    public static void logout(Context ctx) {
        prefs(ctx).edit().clear().apply();
    }

    /**
     * Check whether there is an active login session.
     */
    public static boolean isLoggedIn(Context ctx) {
        return prefs(ctx).getBoolean(KEY_LOGGED_IN, false);
    }

    /**
     * Phone number used for login (may be null).
     */
    public static String getPhone(Context ctx) {
        return prefs(ctx).getString(KEY_PHONE, null);
    }
} 
