package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class VoucherDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        // Nhận dữ liệu từ Intent, có giá trị mặc định nếu null
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

        // Gán dữ liệu cho view, kiểm tra null trước khi setText
        TextView tvCode = findViewById(R.id.tvVoucherCode);
        if (tvCode != null) tvCode.setText(code != null ? code : "");
        TextView tvDesc = findViewById(R.id.tvVoucherDesc);
        if (tvDesc != null) tvDesc.setText(desc != null ? desc : "");
        TextView tvPoint = findViewById(R.id.tvVoucherPoint);
        if (tvPoint != null) tvPoint.setText(point != null ? point : "");
        TextView tvTime = findViewById(R.id.tvVoucherTime);
        if (tvTime != null) tvTime.setText(time != null ? time : "");
        TextView tvBenefit = findViewById(R.id.tvVoucherBenefit);
        if (tvBenefit != null) tvBenefit.setText(benefit != null ? benefit : "");
        TextView tvProduct = findViewById(R.id.tvVoucherProduct);
        if (tvProduct != null) tvProduct.setText(product != null ? product : "");
        TextView tvPayment = findViewById(R.id.tvVoucherPayment);
        if (tvPayment != null) tvPayment.setText(payment != null ? payment : "");
        TextView tvShipping = findViewById(R.id.tvVoucherShipping);
        if (tvShipping != null) tvShipping.setText(shipping != null ? shipping : "");
        TextView tvDevice = findViewById(R.id.tvVoucherDevice);
        if (tvDevice != null) tvDevice.setText(device != null ? device : "");
        TextView tvCondition = findViewById(R.id.tvVoucherCondition);
        if (tvCondition != null) tvCondition.setText(condition != null ? condition : "");

        Button btnExchange = findViewById(R.id.btnExchange);
        if (btnExchange != null) {
            btnExchange.setOnClickListener(v ->
                Toast.makeText(this, "Bạn đã chọn đổi điểm cho voucher này!", Toast.LENGTH_SHORT).show()
            );
        }
    }
} 