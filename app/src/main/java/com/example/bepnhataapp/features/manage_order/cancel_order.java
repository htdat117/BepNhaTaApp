package com.example.bepnhataapp.features.manage_order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Order;
import com.example.bepnhataapp.common.model.OrderStatus;

public class cancel_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancel_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bổ sung UI và logic
        TextView tvOrderInfo = findViewById(R.id.tvOrderInfo);
        EditText edtReason = findViewById(R.id.edtReason);
        Button btnCancel = findViewById(R.id.btnCancelOrder);
        // Giả lập lấy orderId từ intent
        long orderId = getIntent().getLongExtra("orderId", -1);
        if (orderId != -1) {
            // TODO: Lấy thông tin đơn hàng từ DB và hiển thị
            tvOrderInfo.setText("Huỷ đơn hàng #" + orderId);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Cập nhật trạng thái đơn hàng sang CANCELED và lưu lý do
                finish();
            }
        });
    }
}