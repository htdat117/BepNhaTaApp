package com.example.bepnhataapp.features.point;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.bepnhataapp.R;

public class PointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity hiện tại
                }
            });
        }

        TextView tvHistory = findViewById(R.id.tvHistory);
        if (tvHistory != null) {
            tvHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PointActivity.this, PointHistoryActivity.class);
                    startActivity(intent);
                }
            });
        }

        AppCompatButton btnExchange = findViewById(R.id.btnExchange);
        if (btnExchange != null) {
            btnExchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PointActivity.this, com.example.bepnhataapp.features.voucher.VoucherActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
} 