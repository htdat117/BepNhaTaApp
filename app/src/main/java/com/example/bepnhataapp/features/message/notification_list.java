package com.example.bepnhataapp.features.message;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bepnhataapp.R;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.bepnhataapp.common.model.NotificationItem;
import java.util.ArrayList;
import java.util.Calendar;
import com.example.bepnhataapp.common.base.BaseActivity;

public class notification_list extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Kéo nền cam phủ lên status bar như trang Profile
        androidx.activity.EdgeToEdge.enable(this);

        View headerCt = findViewById(R.id.header_container);
        View mainRoot = findViewById(R.id.main);
        if(headerCt!=null){
            ViewCompat.setOnApplyWindowInsetsListener(headerCt,(v,insets)->{
                Insets sb = insets.getInsets(WindowInsetsCompat.Type.statusBars());
                v.setPadding(v.getPaddingLeft(), sb.top, v.getPaddingRight(), v.getPaddingBottom());
                // giữ root top padding =0
                if(mainRoot!=null) mainRoot.setPadding(mainRoot.getPaddingLeft(),0,mainRoot.getPaddingRight(),mainRoot.getPaddingBottom());
                return insets;
            });
        }

        // Container LinearLayout để chứa toàn bộ danh sách thông báo (header + list động)
        android.widget.LinearLayout ctContainer = findViewById(R.id.ctNotification);

        // Tải thông báo từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long userId = com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)? getCurrentUserId():0;
        db.collection("notifications")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener((QuerySnapshot snapshots)->{
                    Calendar now = Calendar.getInstance();

                    // Gom tất cả thông báo theo key "Hôm nay", "Hôm qua" hoặc ngày cụ thể dd/MM/yyyy
                    java.util.Map<String, java.util.List<NotificationItem>> mapByDate = new java.util.HashMap<>();
                    java.util.Map<String, Long> mapDateToEpoch = new java.util.HashMap<>();

                    Calendar calYesterday = (Calendar) now.clone();
                    calYesterday.add(Calendar.DAY_OF_YEAR,-1);

                    for(DocumentSnapshot doc: snapshots.getDocuments()){
                        String id = doc.getId();
                        String title = doc.getString("title");
                        String body = doc.getString("body");
                        long ts = doc.contains("timestamp")? doc.getLong("timestamp") : System.currentTimeMillis();
                        boolean read = doc.contains("read") && Boolean.TRUE.equals(doc.getBoolean("read"));
                        String status = doc.getString("status");
                        NotificationItem it = new NotificationItem(id,title,body,ts,read,status);

                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.setTimeInMillis(ts);

                        boolean isToday = cal.get(Calendar.YEAR)==now.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR)==now.get(Calendar.DAY_OF_YEAR);

                        boolean isYesterday = cal.get(Calendar.YEAR)==calYesterday.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR)==calYesterday.get(Calendar.DAY_OF_YEAR);

                        String key;
                        if(isToday){
                            key = "Hôm nay";
                            mapDateToEpoch.put(key, now.getTimeInMillis());
                        }else if(isYesterday){
                            key = "Hôm qua";
                            mapDateToEpoch.put(key, calYesterday.getTimeInMillis());
                        }else{
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                            key = sdf.format(new java.util.Date(ts));
                            mapDateToEpoch.put(key, ts - (ts % (24*60*60*1000L))); // epoch at start of day
                        }
                        mapByDate.computeIfAbsent(key,k-> new java.util.ArrayList<>()).add(it);
                    }

                    // Xóa toàn bộ view cũ và dựng lại danh sách
                    if(ctContainer!=null){
                        ctContainer.removeAllViews();

                        // Sắp xếp ngày giảm dần theo epoch đã lưu
                        java.util.List<String> dates = new java.util.ArrayList<>(mapByDate.keySet());
                        java.util.Collections.sort(dates,(a,b)-> mapDateToEpoch.get(b).compareTo(mapDateToEpoch.get(a)) );

                        android.content.res.Resources res = getResources();

                        for(String dateStr: dates){
                            java.util.List<NotificationItem> groupList = mapByDate.get(dateStr);

                            // Header row (title + mark all)
                            android.widget.LinearLayout rowHeader = new android.widget.LinearLayout(notification_list.this);
                            rowHeader.setOrientation(android.widget.LinearLayout.HORIZONTAL);
                            android.widget.LinearLayout.LayoutParams rowLp = new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                            rowLp.topMargin = (int) (12*res.getDisplayMetrics().density);
                            rowHeader.setLayoutParams(rowLp);

                            android.widget.TextView tvHeader = new android.widget.TextView(notification_list.this);
                            tvHeader.setText(dateStr);
                            tvHeader.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP,16);
                            tvHeader.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                            tvHeader.setTextColor(res.getColor(R.color.dark1));
                            android.widget.LinearLayout.LayoutParams tvLp = new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,1f);
                            tvHeader.setLayoutParams(tvLp);

                            android.widget.TextView tvMark = new android.widget.TextView(notification_list.this);
                            tvMark.setText("Đánh dấu đã đọc");
                            tvMark.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP,14);
                            tvMark.setTextColor(res.getColor(R.color.dark1));
                            tvMark.setGravity(android.view.Gravity.END);

                            rowHeader.addView(tvHeader);
                            rowHeader.addView(tvMark);

                            ctContainer.addView(rowHeader);

                            // ListView cho nhóm này
                            android.widget.ListView lv = new android.widget.ListView(notification_list.this);
                            android.widget.LinearLayout.LayoutParams lvLp = new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                            lv.setLayoutParams(lvLp);
                            NotificationAdapter ad = new NotificationAdapter(notification_list.this, groupList);
                            lv.setAdapter(ad);

                            ctContainer.addView(lv);

                            // Xử lý click => đánh dấu đã đọc item đơn
                            lv.setOnItemClickListener((parent, view, position, id) -> {
                                NotificationItem item = groupList.get(position);
                                handleRead(item, ad, ad);
                            });

                            // Xử lý click => mark all
                            tvMark.setOnClickListener(v -> {
                                java.util.List<String> ids = new java.util.ArrayList<>();
                                for(NotificationItem n: groupList){ if(!n.isRead()){ ids.add(n.getId()); n.setRead(true);} }
                                if(!ids.isEmpty()){
                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                    for(String id: ids){ db2.collection("notifications").document(id).update("read", true); }
                                }
                                ad.notifyDataSetChanged();
                                updateBadgeCounts();
                            });
                        }
                    }

                    // Gắn fragment bottom navigation và chọn tab trang chủ mặc định
                    setupBottomNavigationFragment(R.id.nav_home);
                });
    }

    private long getCurrentUserId(){
        String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
        if(phone==null) return 0;
        com.example.bepnhataapp.common.dao.CustomerDao dao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
        com.example.bepnhataapp.common.model.Customer c = dao.findByPhone(phone);
        return c!=null?c.getCustomerID():0;
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private void handleRead(NotificationItem item, NotificationAdapter mainAdapter, NotificationAdapter other){
        if(item==null||item.isRead()) return;
        item.setRead(true);
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("notifications").document(item.getId()).update("read",true);
        mainAdapter.notifyDataSetChanged();
        other.notifyDataSetChanged();
        updateBadgeCounts();
    }
}
