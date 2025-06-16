package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity hiện tại
                }
            });
        }

        RecyclerView rvVouchers = findViewById(R.id.rvVouchers);
        if (rvVouchers != null) {
            rvVouchers.setLayoutManager(new LinearLayoutManager(this));
            List<VoucherItem> voucherList = new ArrayList<>();
            // Thêm dữ liệu voucher mẫu
            voucherList.add(new VoucherItem("GIAM20%", "Giảm 20% đơn hàng chào mừng tháng 4, giảm tối đa 60K", "30/04/2024"));
            voucherList.add(new VoucherItem("FREESHIP", "Miễn phí vận chuyển cho đơn hàng từ 100K", "15/05/2024"));
            voucherList.add(new VoucherItem("SUMMER50K", "Giảm 50K cho đơn hàng từ 200K", "31/07/2024"));
            voucherList.add(new VoucherItem("BUY1GET1", "Mua 1 tặng 1 món bất kỳ", "10/06/2024"));
            voucherList.add(new VoucherItem("NEWUSER", "Giảm 30% cho khách hàng mới", "31/12/2024"));

            VoucherDisplayAdapter adapter = new VoucherDisplayAdapter(voucherList, this);
            rvVouchers.setAdapter(adapter);
        }
    }
}