package com.example.feature.voucher;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.R;

public class VoucherDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        // Nhận dữ liệu từ Intent
        String code = getIntent().getStringExtra("code");
        String desc = getIntent().getStringExtra("desc");
        String point = getIntent().getStringExtra("point");
        String time = getIntent().getStringExtra("time");
        String benefit = getIntent().getStringExtra("benefit");
        String product = getIntent().getStringExtra("product");
        String payment = getIntent().getStringExtra("payment");
        String shipping = getIntent().getStringExtra("shipping");
        String device = getIntent().getStringExtra("device");
        String condition = getIntent().getStringExtra("condition");

        ((TextView)findViewById(R.id.tvVoucherCode)).setText(code);
        ((TextView)findViewById(R.id.tvVoucherDesc)).setText(desc);
        ((TextView)findViewById(R.id.tvVoucherPoint)).setText(point);
        ((TextView)findViewById(R.id.tvVoucherTime)).setText(time);
        ((TextView)findViewById(R.id.tvVoucherBenefit)).setText(benefit);
        ((TextView)findViewById(R.id.tvVoucherProduct)).setText(product);
        ((TextView)findViewById(R.id.tvVoucherPayment)).setText(payment);
        ((TextView)findViewById(R.id.tvVoucherShipping)).setText(shipping);
        ((TextView)findViewById(R.id.tvVoucherDevice)).setText(device);
        ((TextView)findViewById(R.id.tvVoucherCondition)).setText(condition);

        Button btnExchange = findViewById(R.id.btnExchange);
        btnExchange.setOnClickListener(v ->
            Toast.makeText(this, "Bạn đã chọn đổi điểm cho voucher này!", Toast.LENGTH_SHORT).show()
        );
    }
} 