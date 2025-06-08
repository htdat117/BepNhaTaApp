package com.example.bepnhataapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.ViewGroup;

public class BmiBmrCalculatorActivity extends AppCompatActivity {

    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale;
    private EditText editTextAge, editTextHeight, editTextWeight;
    private Button btnCalculateBmi, btnCalculateBmr, btnReset;
    private Button btnBmiBmrTab, btnCaloTab; // Assuming you might need these buttons for switching tabs
    private View tabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_bmr_calculator);

        // Ánh xạ các View từ layout
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        editTextAge = findViewById(R.id.editTextAge);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        btnCalculateBmi = findViewById(R.id.btnCalculateBmi);
        btnCalculateBmr = findViewById(R.id.btnCalculateBmr);
        btnReset = findViewById(R.id.btnReset);

        // Ánh xạ các nút tab (nếu cần thiết cho xử lý chuyển tab)
        tabIndicator = findViewById(R.id.tabIndicator);
        btnBmiBmrTab = findViewById(R.id.btnBmiBmrTab);
        btnCaloTab = findViewById(R.id.btnCaloTab);

        // Đặt chiều rộng tabIndicator bằng với btnBmiBmrTab khi layout xong
        btnBmiBmrTab.post(new Runnable() {
            @Override
            public void run() {
                int width = btnBmiBmrTab.getWidth();
                ViewGroup.LayoutParams params = tabIndicator.getLayoutParams();
                params.width = width;
                tabIndicator.setTranslationX(0);

            }
        });

        // Gán sự kiện click cho các nút hành động
        btnCalculateBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Xử lý logic tính BMI tại đây
                // Lấy dữ liệu từ editTextAge, editTextHeight, editTextWeight và radioGroupGender
            }
        });

        btnCalculateBmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Xử lý logic tính BMR tại đây
                // Lấy dữ liệu từ editTextAge, editTextHeight, editTextWeight và radioGroupGender
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Xử lý logic đặt lại tại đây
                // Xóa nội dung các EditText, đặt lại RadioButton
            }
        });

        btnBmiBmrTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateTab(0);
                btnBmiBmrTab.setTextColor(Color.WHITE);
                btnCaloTab.setTextColor(Color.BLACK);
                // TODO: Hiển thị nội dung BMI/BMR nếu có
            }
        });

        btnCaloTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width = btnBmiBmrTab.getWidth();
                animateTab(width);
                // Nếu muốn chuyển màn hình thì giữ lại intent như cũ
                Intent intent = new Intent(BmiBmrCalculatorActivity.this, CaloCalculatorActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void animateTab(int toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(tabIndicator, "translationX", toX);
        animator.setDuration(200);
        animator.start();
    }

    // TODO: Viết các hàm tính BMI và BMR tại đây
} 