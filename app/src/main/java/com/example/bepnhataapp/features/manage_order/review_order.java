package com.example.bepnhataapp.features.manage_order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.OrderLineDao;
import com.example.bepnhataapp.common.model.OrderLine;

import java.util.List;

public class review_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xử lý back
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Lấy orderId từ intent
        long orderId = getIntent().getLongExtra("orderId", -1);
        RecyclerView rcProduct = findViewById(R.id.rc_Product);
        if (orderId != -1 && rcProduct != null) {
            OrderLineDao dao = new OrderLineDao(this);
            List<OrderLine> orderLines = dao.getByOrder(orderId);
            rcProduct.setLayoutManager(new LinearLayoutManager(this));
            rcProduct.setAdapter(new OrderProductAdapter(orderLines));
        }

        // Xử lý gửi đánh giá
        Button btnSubmit = findViewById(R.id.btnSubmit);
        EditText edtReview = findViewById(R.id.edtReview);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String review = edtReview.getText().toString();
                // TODO: Lưu đánh giá vào DB
                Toast.makeText(review_order.this, "Đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}