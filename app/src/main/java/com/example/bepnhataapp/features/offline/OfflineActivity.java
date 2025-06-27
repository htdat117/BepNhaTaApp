package com.example.bepnhataapp.features.offline;

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
        btnRetry.setOnClickListener(v -> recreate());
    }
} 