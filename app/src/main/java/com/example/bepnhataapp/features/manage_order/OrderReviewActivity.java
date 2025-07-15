package com.example.bepnhataapp.features.manage_order;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class OrderReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        // Lấy orderId từ intent
        long orderId = getIntent().getLongExtra("orderId", -1);
        // TODO: Lấy thông tin đơn hàng từ DB và hiển thị lên giao diện

        TextView tvOrderCode = findViewById(R.id.tvOrderCode);

        TextView tvProductName = findViewById(R.id.tvProductName);
        TextView tvVariant = findViewById(R.id.tvVariant);
        TextView tvOldPrice = findViewById(R.id.tvOldPrice);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvQuantity = findViewById(R.id.tvQuantity);
        TextView tvOrderTotal = findViewById(R.id.tvOrderTotal);
        ImageView imgProduct = findViewById(R.id.imgProduct);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText edtReview = findViewById(R.id.edtReview);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            String comment = edtReview.getText().toString();
            // TODO: Lưu đánh giá vào DB
            android.widget.Toast.makeText(this, "Đã gửi đánh giá!", android.widget.Toast.LENGTH_SHORT).show();
            finish();
        });
    }
} 
