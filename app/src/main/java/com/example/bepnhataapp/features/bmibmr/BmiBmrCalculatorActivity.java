package com.example.bepnhataapp.features.bmibmr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.view.ViewGroup;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;

import com.example.bepnhataapp.features.calocal.CaloCalculatorActivity;
import com.example.bepnhataapp.R;

public class BmiBmrCalculatorActivity extends AppCompatActivity {
    private static final String TAG = "BmiBmrCalculatorActivity";

    private Button btnBmiBmrTab, btnCaloTab;
    private View tabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bmi_bmr_calo);
        Log.d(TAG, "onCreate: Activity started");

        // Ánh xạ các View từ layout chính
        tabIndicator = findViewById(R.id.tabIndicator);
        btnBmiBmrTab = findViewById(R.id.btnBmiBmrTab);
        btnCaloTab = findViewById(R.id.btnCaloTab);

        if (tabIndicator == null || btnBmiBmrTab == null || btnCaloTab == null) {
            Log.e(TAG, "onCreate: Failed to find views");
            return;
        }

        // Load fragment BMI/BMR content
        try {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new BmiBmrContentFragment())
                .commit();
            Log.d(TAG, "onCreate: Fragment loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Failed to load fragment", e);
        }

        // Đặt chiều rộng tabIndicator bằng với btnBmiBmrTab khi layout xong
        btnBmiBmrTab.post(new Runnable() {
            @Override
            public void run() {
                try {
                    int width = btnBmiBmrTab.getWidth();
                    ViewGroup.LayoutParams params = tabIndicator.getLayoutParams();
                    params.width = width;
                    tabIndicator.setTranslationX(0);
                    Log.d(TAG, "Tab indicator width set to: " + width);
                } catch (Exception e) {
                    Log.e(TAG, "Error setting tab indicator width", e);
                }
            }
        });

        // Gán sự kiện click cho các nút tab
        btnBmiBmrTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "BMI/BMR tab clicked");
                animateTab(0);
                btnBmiBmrTab.setTextColor(Color.WHITE);
                btnCaloTab.setTextColor(Color.BLACK);
                try {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new BmiBmrContentFragment())
                        .commit();
                    Log.d(TAG, "BMI/BMR fragment loaded");
                } catch (Exception e) {
                    Log.e(TAG, "Failed to load BMI/BMR fragment", e);
                }
            }
        });

        btnCaloTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Calo tab clicked");
                int width = btnBmiBmrTab.getWidth();
                animateTab(width);
                btnBmiBmrTab.setTextColor(Color.BLACK);
                btnCaloTab.setTextColor(Color.WHITE);
                Intent intent = new Intent(BmiBmrCalculatorActivity.this, CaloCalculatorActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void animateTab(int toX) {
        try {
            ObjectAnimator animator = ObjectAnimator.ofFloat(tabIndicator, "translationX", toX);
            animator.setDuration(200);
            animator.start();
            Log.d(TAG, "Tab animation started to position: " + toX);
        } catch (Exception e) {
            Log.e(TAG, "Error animating tab", e);
        }
    }
} 