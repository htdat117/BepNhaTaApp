package com.example.bepnhataapp.features.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class ShippingInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_info);

        // set header title
        ((android.widget.TextView) findViewById(R.id.txtContent)).setText("Thông tin giao hàng");
        findViewById(R.id.txtChange).setVisibility(View.GONE);
        findViewById(R.id.iv_logo).setOnClickListener(v->finish());

        // attach clear buttons
        attachClear(R.id.edtName);
        attachClear(R.id.edtPhone);
        attachClear(R.id.edtEmail);
        attachClear(R.id.edtCity);
        attachClear(R.id.edtDistrict);
        attachClear(R.id.edtAddress);

        findViewById(R.id.btnSubmit).setOnClickListener(v->{
            // validation TODO
            setResult(RESULT_OK);
            finish();
        });
    }
    private void attachClear(int includeId){
        View inc = findViewById(includeId);
        if (inc == null) return; // layout id not present

        EditText et = inc.findViewById(R.id.etInput);
        ImageButton btn = inc.findViewById(R.id.btnClear);
        if (et != null && btn != null) {
            btn.setOnClickListener(v -> et.setText(""));
        }
    }
} 