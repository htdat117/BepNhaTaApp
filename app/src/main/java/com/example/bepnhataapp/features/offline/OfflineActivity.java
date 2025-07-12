package com.example.bepnhataapp.features.offline;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;

public class OfflineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        Button btnOffline = findViewById(R.id.btnOffline);
        Button btnRetry = findViewById(R.id.btnRetry);

        btnOffline.setOnClickListener(v -> {
            // Chuyển sang màn hình nội dung offline
            startActivity(new android.content.Intent(OfflineActivity.this, DownloadedContentActivity.class));
        });
        btnRetry.setOnClickListener(v -> {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
            if (isConnected) {
                finish(); // Đã có mạng, quay về trang chính hoặc reload app
            } else {
                recreate(); // Vẫn chưa có mạng, chỉ reload lại trang offline
            }
        });
    }
} 
