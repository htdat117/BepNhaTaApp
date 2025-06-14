package com.example.bepnhataapp.features.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class ConfirmPaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        ImageView back = findViewById(R.id.iv_logo);
        TextView title = findViewById(R.id.txtContent);
        TextView change = findViewById(R.id.txtChange);
        if(title!=null) title.setText("Xác nhận thanh toán");
        if(change!=null) change.setVisibility(View.GONE);
        if(back!=null) back.setOnClickListener(v -> finish());

        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            android.content.Intent it=new android.content.Intent(this, PaymentSuccessActivity.class);
            startActivity(it);
            finish();
        });
    }
} 