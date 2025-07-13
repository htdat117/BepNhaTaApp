package com.example.bepnhataapp.common.utils;

public class ReminderReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        String slot = intent.getStringExtra("meal_slot");
        if(slot==null) slot="bữa ăn";
        android.app.NotificationManager nm = (android.app.NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        String chId = "mealplan_reminder";
        if(android.os.Build.VERSION.SDK_INT>=26){
            android.app.NotificationChannel ch = new android.app.NotificationChannel(chId, "MealPlan Reminder", android.app.NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(ch);
        }
        android.app.Notification noti = new androidx.core.app.NotificationCompat.Builder(context, chId)
                .setSmallIcon(com.example.bepnhataapp.R.drawable.ic_notification)
                .setContentTitle("Đến giờ cho \""+slot+"\"")
                .setContentText("Hãy chuẩn bị và thưởng thức bữa ăn của bạn!")
                .setAutoCancel(true)
                .build();
        nm.notify((int)System.currentTimeMillis(), noti);
    }
} 