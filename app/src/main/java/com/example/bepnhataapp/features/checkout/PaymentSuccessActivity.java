package com.example.bepnhataapp.features.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.home.HomeActivity;

public class PaymentSuccessActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        // header title
        ((android.widget.TextView)findViewById(R.id.tv_login)).setText("Hồ Tiến Đạt"); // demo
        findViewById(R.id.btnTrackOrder).setOnClickListener(v->{
            // TODO open order tracking
        });
        findViewById(R.id.btnGoHome).setOnClickListener(v->{
            Intent i=new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }
} 