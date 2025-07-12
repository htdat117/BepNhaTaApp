package com.example.bepnhataapp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class EmailOtpService {
    private static final String PREF_OTP = "otp_email_store";
    private static final String KEY_CODE_PREFIX = "code_"; // code_<email>
    private static final String KEY_EXP_PREFIX = "exp_";  // exp_<email>
    private static final long OTP_EXP_MS = 5 * 60 * 1000;
    private static final int OTP_LENGTH = 6;
    private static final String CHANNEL_ID = "email_otp";
    private static final Random random = new Random();

    private static String genCode() {
        int n = 100000 + random.nextInt(900000);
        return String.format(Locale.getDefault(), "%06d", n);
    }

    public static void sendOtp(Context ctx, String email) {
        if (email == null || email.isEmpty()) return;
        String code = genCode();
        long exp = System.currentTimeMillis() + OTP_EXP_MS;
        SharedPreferences sp = ctx.getSharedPreferences(PREF_OTP, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_CODE_PREFIX + email, code).putLong(KEY_EXP_PREFIX + email, exp).apply();
        // Show notification as placeholder for real email
        showNotification(ctx, email, code);
    }

    public static boolean verifyOtp(Context ctx, String email, String input) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_OTP, Context.MODE_PRIVATE);
        String saved = sp.getString(KEY_CODE_PREFIX + email, null);
        long exp = sp.getLong(KEY_EXP_PREFIX + email, 0);
        if (saved == null) return false;
        if (System.currentTimeMillis() > exp) return false;
        return saved.equals(input.trim());
    }

    public static void clear(Context ctx, String email) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_OTP, Context.MODE_PRIVATE);
        sp.edit().remove(KEY_CODE_PREFIX + email).remove(KEY_EXP_PREFIX + email).apply();
    }

    private static void showNotification(Context ctx, String email, String code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = ctx.getSystemService(NotificationManager.class);
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Email OTP", NotificationManager.IMPORTANCE_HIGH);
                nm.createNotificationChannel(ch);
            }
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("OTP cho email " + email)
                .setContentText("Mã xác thực: " + code)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat.from(ctx).notify((int) System.currentTimeMillis(), b.build());

        // Also show Toast so testers see OTP immediately
        Toast.makeText(ctx, "OTP: " + code, Toast.LENGTH_LONG).show();
    }
} 
