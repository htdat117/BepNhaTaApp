package com.example.bepnhataapp.features.calocal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.bmibmr.BmiBmrCalculatorActivity;

public class CaloCalculatorActivity extends AppCompatActivity {

    private EditText editTextFoodName, editTextWeight;
    private Button btnSave, btnDelete;
    private Button btnBmiBmrTab, btnCaloTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calo_calculator);

        // Ánh xạ các View từ layout
        editTextFoodName = findViewById(R.id.editTextFoodName);
        editTextWeight = findViewById(R.id.editTextWeight);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);


        // Chuyển sang trang BMI/BMR khi bấm tab
        btnBmiBmrTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaloCalculatorActivity.this, BmiBmrCalculatorActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Nút Lưu và Xóa: chỉ khởi tạo, chưa xử lý logic
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Xử lý logic lưu món ăn ở đây
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFoodName.setText("");
                editTextWeight.setText("");
            }
        });
    }
} 