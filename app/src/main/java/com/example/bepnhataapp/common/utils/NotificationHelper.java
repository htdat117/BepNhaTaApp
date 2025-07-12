package com.example.bepnhataapp.common.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.home.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.example.bepnhataapp.common.model.OrderStatus;

public final class NotificationHelper {
    private NotificationHelper(){}

    private static final String CHANNEL_ID = "order_updates";

    /**
     * Lưu thông báo lên Cloud Firestore và đồng thời hiển thị thông báo cục bộ trên máy.
     * @param ctx  context
     * @param orderId  mã đơn hàng
     * @param totalPrice  tổng giá trị đơn
     */
    public static void pushOrderStatus(Context ctx, long orderId, double totalPrice, OrderStatus status){
        if(orderId<=0 || status==null) return;
        String title = status.getDisplayName();
        String code = "BNT"+orderId;
        String body;
        switch (status){
            case WAIT_CONFIRM:
                body = "Đơn hàng "+code+" đang được chờ Bếp Nhà Ta xác nhận. Bếp Nhà Ta sẽ thông tin đến bạn trong thời gian sớm nhất!";
                break;
            case OUT_FOR_DELIVERY:
                body = "Shipper đang giao đơn hàng "+code+" đến bạn. Hãy giữ điện thoại bên mình nhé!";
                break;
            case DELIVERED:
                body = "Đơn hàng "+code+" đã được giao thành công. Cảm ơn bạn đã ủng hộ Bếp Nhà Ta!";
                break;
            case CANCELED:
                body = "Đơn hàng "+code+" đã bị huỷ. Nếu cần hỗ trợ, vui lòng liên hệ Bếp Nhà Ta qua mục Trợ giúp.";
                break;
            case RETURNED:
                body = "Số tiền của đơn hàng "+code+" đã được hoàn về phương thức thanh toán của bạn.";
                break;
            case WAIT_PICKUP:
            default:
                body = "Bếp Nhà Ta đã nhận được thanh toán cho đơn hàng "+code+". Đơn hàng của bạn sẽ được xử lý trong thời gian sớm nhất!";
                break;
        }

        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String,Object> data = new HashMap<>();
            long userId = SessionManager.isLoggedIn(ctx) ? getCurrentCustomerId(ctx) : 0;
            data.put("userId", userId);
            data.put("orderId", orderId);
            data.put("title", title);
            data.put("body", body);
            data.put("status", status.name());
            data.put("timestamp", System.currentTimeMillis());
            data.put("read", false);
            db.collection("notifications").add(data);
        }catch(Exception ignore){ }

        showLocalNotification(ctx, title, body);
    }

    public static void pushEarnPoint(Context ctx, int point, String action, String description) {
        String title = (point > 0 ? "Bạn vừa nhận được " + point + " điểm tích lũy!" : "Bạn vừa sử dụng " + (-point) + " điểm tích lũy!");
        String body = (description != null && !description.isEmpty()) ? description : ("Hoạt động: " + action);
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String,Object> data = new HashMap<>();
            long userId = SessionManager.isLoggedIn(ctx) ? getCurrentCustomerId(ctx) : 0;
            data.put("userId", userId);
            data.put("title", title);
            data.put("body", body);
            data.put("status", "EARN_POINT");
            data.put("timestamp", System.currentTimeMillis());
            data.put("read", false);
            db.collection("notifications").add(data);
        } catch(Exception ignore) { }
        showLocalNotification(ctx, title, body);
    }

    /** Hiển thị Notification native */
    public static void showLocalNotification(Context ctx, String title, String body){
        createChannelIfNeeded(ctx);
        Intent intent = new Intent(ctx, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(ctx).notify((int)(System.currentTimeMillis() & 0xfffffff), builder.build());
    }

    private static void createChannelIfNeeded(Context ctx){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            if(nm.getNotificationChannel(CHANNEL_ID)==null){
                NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Cập nhật đơn hàng", NotificationManager.IMPORTANCE_DEFAULT);
                nm.createNotificationChannel(ch);
            }
        }
    }

    private static long getCurrentCustomerId(Context ctx){
        String phone = SessionManager.getPhone(ctx);
        if(phone==null) return 0;
        com.example.bepnhataapp.common.dao.CustomerDao dao = new com.example.bepnhataapp.common.dao.CustomerDao(ctx);
        com.example.bepnhataapp.common.model.Customer c = dao.findByPhone(phone);
        return c!=null?c.getCustomerID():0;
    }
} 
