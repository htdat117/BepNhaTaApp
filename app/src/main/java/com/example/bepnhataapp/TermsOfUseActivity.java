package com.example.bepnhataapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class TermsOfUseActivity extends AppCompatActivity {

    private Button btnDieuKhoan, btnBaoMat, btnGiaoHang, btnDoiTra;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ánh xạ các nút
        btnDieuKhoan = findViewById(R.id.btnDieuKhoan);
        btnBaoMat = findViewById(R.id.btnBaoMat);
        btnGiaoHang = findViewById(R.id.btnGiaoHang);
        btnDoiTra = findViewById(R.id.btnDoiTra);
        btnBack = findViewById(R.id.btnBack);

        // Gán sự kiện cho nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Gán sự kiện cho các nút chuyển trang
        btnDieuKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsOfUseActivity.this, TermsOfUseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnBaoMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsOfUseActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsOfUseActivity.this, ShippingPolicyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDoiTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsOfUseActivity.this, ReturnPolicyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
} 