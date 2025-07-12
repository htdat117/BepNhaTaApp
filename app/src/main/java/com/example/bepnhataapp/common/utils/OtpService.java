package com.example.bepnhataapp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;
import java.util.Random;

/**
 * Simple reusable OTP utility.
 * - Generates 6-digit OTP, stores in SharedPreferences with 5-minute expiry.
 * - Sends via SMS if SEND_SMS permission is granted; otherwise shows a Toast (for dev/testing).
 * - Provides verify method.
 */
public class OtpService {

    private static final String PREF_OTP = "otp_store";
    private static final String KEY_CODE_PREFIX = "code_"; // code_<phone>
    private static final String KEY_EXP_PREFIX = "exp_";  // exp_<phone>
    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXP_MS = 5 * 60 * 1000; // 5 minutes

    private static final String CHANNEL_ID = "otp_channel";

    private static final Random random = new Random();

    private static String generateOtpCode() {
        int num = 100000 + random.nextInt(900000); // 100000-999999
        return String.format(Locale.getDefault(), "%06d", num);
    }

    public static void sendOtp(Context ctx, String phone) {
        if (phone == null || phone.isEmpty()) return;
        String code = generateOtpCode();
        long exp = System.currentTimeMillis() + OTP_EXP_MS;

        SharedPreferences sp = ctx.getSharedPreferences(PREF_OTP, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_CODE_PREFIX + phone, code)
                .putLong(KEY_EXP_PREFIX + phone, exp)
                .apply();

        // Always show notification so dev có thể nhìn thấy OTP, không cần phụ thuộc SMS
        showOtpNotification(ctx, code);

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone, null, "Mã OTP Bếp Nhà Ta: " + code, null, null);
        } catch (Exception e) {
            // Không gửi được SMS cũng không sao
            Log.w("OtpService", "SMS not sent (this is fine for dev/emulator)", e);
        }
    }

    public static boolean verifyOtp(Context ctx, String phone, String inputCode) {
        if (phone == null || inputCode == null) return false;
        SharedPreferences sp = ctx.getSharedPreferences(PREF_OTP, Context.MODE_PRIVATE);
        String saved = sp.getString(KEY_CODE_PREFIX + phone, null);
        long exp = sp.getLong(KEY_EXP_PREFIX + phone, 0);
        if (saved == null) return false;
        if (System.currentTimeMillis() > exp) return false; // expired
        return saved.equals(inputCode.trim());
    }

    /**
     * Optional: clear stored OTP after successful verification to prevent reuse.
     */
    public static void clearOtp(Context ctx, String phone) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_OTP, Context.MODE_PRIVATE);
        sp.edit()
                .remove(KEY_CODE_PREFIX + phone)
                .remove(KEY_EXP_PREFIX + phone)
                .apply();
    }

    private static void ensureChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = ctx.getSystemService(NotificationManager.class);
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel chan = new NotificationChannel(CHANNEL_ID, "OTP Notifications", NotificationManager.IMPORTANCE_HIGH);
                chan.setDescription("Thông báo chứa mã OTP đăng nhập/đăng ký");
                nm.createNotificationChannel(chan);
            }
        }
    }

    private static void showOtpNotification(Context ctx, String code) {
        ensureChannel(ctx);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Mã OTP Bếp Nhà Ta")
                .setContentText("Mã xác thực của bạn: " + code)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(ctx).notify((int) System.currentTimeMillis(), builder.build());

        // Đồng thời Toast để chắc chắn người dùng nhìn thấy
        Toast.makeText(ctx, "OTP: " + code, Toast.LENGTH_LONG).show();
    }
} 
