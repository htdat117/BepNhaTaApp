package com.example.bepnhataapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OfflineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        Button btnOffline = findViewById(R.id.btnOffline);
        Button btnRetry = findViewById(R.id.btnRetry);

//        btnOffline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Chuyển sang màn hình nội dung offline
//                Intent intent = new Intent(OfflineActivity.this, OfflineContentActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnRetry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Reload lại activity
//                recreate();
//            }
//        });
  }
} 