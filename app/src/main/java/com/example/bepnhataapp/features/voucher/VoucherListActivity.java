package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;

public class VoucherListActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "extra_title";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        TextView tvTitle = findViewById(R.id.tvTitle);
        if (title != null) {
            tvTitle.setText(title);
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView rvVoucherList = findViewById(R.id.rvVoucherList);
        // TODO: Truyền adapter và dữ liệu vào đây
    }
} 
