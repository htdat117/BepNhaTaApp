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
import com.example.bepnhataapp.common.models.NotificationItem;
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

        // Danh sách thông báo
        android.widget.ListView lvToday = findViewById(R.id.listToday);
        android.widget.ListView lvYesterday = findViewById(R.id.listYesterday);

        java.util.List<NotificationItem> listToday = new ArrayList<>();
        java.util.List<NotificationItem> listYesterday = new ArrayList<>();
        NotificationAdapter todayAdapter = new NotificationAdapter(this, listToday);
        NotificationAdapter yAdapter = new NotificationAdapter(this, listYesterday);
        if(lvToday!=null) lvToday.setAdapter(todayAdapter);
        if(lvYesterday!=null) lvYesterday.setAdapter(yAdapter);

        // Tải thông báo từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long userId = com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)? getCurrentUserId():0;
        db.collection("notifications")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener((QuerySnapshot snapshots)->{
                    listToday.clear();
                    listYesterday.clear();
                    Calendar now = Calendar.getInstance();
                    for(DocumentSnapshot doc: snapshots.getDocuments()){
                        String id = doc.getId();
                        String title = doc.getString("title");
                        String body = doc.getString("body");
                        long ts = doc.contains("timestamp")? doc.getLong("timestamp") : System.currentTimeMillis();
                        boolean read = doc.contains("read") && Boolean.TRUE.equals(doc.getBoolean("read"));
                        String status = doc.getString("status");
                        NotificationItem it = new NotificationItem(id,title,body,ts,read,status);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(ts);
                        boolean isToday = cal.get(Calendar.YEAR)==now.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR)==now.get(Calendar.DAY_OF_YEAR);
                        if(isToday){
                            listToday.add(it);
                        }else{
                            listYesterday.add(it);
                        }
                    }
                    todayAdapter.notifyDataSetChanged();
                    yAdapter.notifyDataSetChanged();
                });

        android.widget.TextView tvMarkToday = findViewById(R.id.tvMarkReadToday);
        android.widget.TextView tvMarkY = findViewById(R.id.tvMarkReadYesterday);

        android.view.View.OnClickListener markAll = v -> {
            java.util.List<String> ids = new java.util.ArrayList<>();
            java.util.List<NotificationItem> target = v.getId()==R.id.tvMarkReadToday? listToday : listYesterday;
            for(NotificationItem n: target){ if(!n.isRead()){ ids.add(n.getId()); n.setRead(true);} }
            if(!ids.isEmpty()){
                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                for(String id: ids){ db2.collection("notifications").document(id).update("read", true); }
            }
            todayAdapter.notifyDataSetChanged();
            yAdapter.notifyDataSetChanged();
            updateBadgeCounts();
        };
        if(tvMarkToday!=null) tvMarkToday.setOnClickListener(markAll);
        if(tvMarkY!=null) tvMarkY.setOnClickListener(markAll);

        // Gắn fragment bottom navigation và chọn tab trang chủ mặc định
        setupBottomNavigationFragment(R.id.nav_home);

        if(lvToday!=null){
            lvToday.setOnItemClickListener((parent,view,position,id)->{
                NotificationItem item=listToday.get(position);
                handleRead(item,todayAdapter,yAdapter);
            });
        }
        if(lvYesterday!=null){
            lvYesterday.setOnItemClickListener((parent,view,position,id)->{
                NotificationItem item=listYesterday.get(position);
                handleRead(item,yAdapter,todayAdapter);
            });
        }
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