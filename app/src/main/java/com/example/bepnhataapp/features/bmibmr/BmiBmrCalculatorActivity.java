package com.example.bepnhataapp.features.bmibmr;

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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.example.bepnhataapp.features.calocal.CaloCalculatorActivity;
import com.example.bepnhataapp.R;

public class BmiBmrCalculatorActivity extends AppCompatActivity {

    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale;
    private EditText editTextAge, editTextHeight, editTextWeight;
    private Button btnCalculateBmi, btnCalculateBmr, btnReset;
    private Button btnBmiBmrTab, btnCaloTab;
    private View tabIndicator;
    private TextView textViewBmiResult, textViewBmrResult, textViewBmiCategory, textViewAdvice, textViewResultTitle;
    private View resultCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bmi_bmr_calo);

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
        textViewBmiResult = findViewById(R.id.textViewBmiResult);
        textViewBmrResult = findViewById(R.id.textViewBmrResult);
        textViewBmiCategory = findViewById(R.id.textViewBmiCategory);
        textViewAdvice = findViewById(R.id.textViewAdvice);
        textViewResultTitle = findViewById(R.id.textViewResultTitle);
        resultCardView = findViewById(R.id.resultCardView);

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
                if (validateInputs()) {
                    int age = Integer.parseInt(editTextAge.getText().toString());
                    double heightCm = Double.parseDouble(editTextHeight.getText().toString());
                    double weight = Double.parseDouble(editTextWeight.getText().toString());
                    double heightM = heightCm / 100;
                    double bmi = calculateBMI(heightM, weight);
                    String bmiCategory = getBMICategory(bmi);
                    double bmr = calculateBMR(age, heightCm, weight, radioMale.isChecked());
                    String advice = getAdvice(bmi);
                    showResult(bmi, bmiCategory, bmr, advice);
                }
            }
        });

        btnCalculateBmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    int age = Integer.parseInt(editTextAge.getText().toString());
                    double heightCm = Double.parseDouble(editTextHeight.getText().toString());
                    double weight = Double.parseDouble(editTextWeight.getText().toString());
                    double heightM = heightCm / 100;
                    double bmi = calculateBMI(heightM, weight);
                    String bmiCategory = getBMICategory(bmi);
                    double bmr = calculateBMR(age, heightCm, weight, radioMale.isChecked());
                    String advice = getAdvice(bmi);
                    showResult(bmi, bmiCategory, bmr, advice);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
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

    private boolean validateInputs() {
        if (editTextAge.getText().toString().isEmpty() ||
            editTextHeight.getText().toString().isEmpty() ||
            editTextWeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!radioMale.isChecked() && !radioFemale.isChecked()) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int age = Integer.parseInt(editTextAge.getText().toString());
            double height = Double.parseDouble(editTextHeight.getText().toString());
            double weight = Double.parseDouble(editTextWeight.getText().toString());

            if (age <= 0 || age > 120) {
                Toast.makeText(this, "Tuổi không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (height <= 0 || height > 300) {
                Toast.makeText(this, "Chiều cao không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (weight <= 0 || weight > 500) {
                Toast.makeText(this, "Cân nặng không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private double calculateBMI(double height, double weight) {
        return weight / (height * height);
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Thiếu cân";
        if (bmi < 25) return "Bình thường";
        if (bmi < 30) return "Thừa cân";
        return "Béo phì";
    }

    private double calculateBMR(int age, double height, double weight, boolean isMale) {
        // Sử dụng công thức Mifflin-St Jeor Equation
        if (isMale) {
            return (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            return (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
    }

    private void showResult(double bmi, String bmiCategory, double bmr, String advice) {
        resultCardView.setVisibility(View.VISIBLE);
        textViewBmiResult.setText(String.format("BMI của bạn là: %.1f", bmi));
        textViewBmiCategory.setText(String.format("Phân loại: %s", bmiCategory));
        textViewBmrResult.setText(String.format("BMR của bạn là: %.0f kcal/ngày", bmr));
        textViewAdvice.setText(String.format("Lời khuyên: %s", advice));
    }

    private String getAdvice(double bmi) {
        if (bmi < 18.5) {
            return "Bạn đang ở mức thiếu cân. Nên bổ sung dinh dưỡng và tăng cường vận động để cải thiện sức khỏe.";
        } else if (bmi < 25) {
            return "Chúc mừng! Bạn có chỉ số BMI bình thường. Hãy duy trì chế độ ăn uống và luyện tập hợp lý.";
        } else if (bmi < 30) {
            return "Bạn đang ở mức thừa cân nhẹ. Nên kiểm soát chế độ ăn uống, tăng cường vận động hàng ngày như đi bộ, bơi lội hoặc tập gym ít nhất 3 buổi/tuần để cải thiện sức khỏe.";
        } else {
            return "Bạn đang ở mức béo phì. Cần có chế độ ăn uống hợp lý, hạn chế đồ ngọt, dầu mỡ và tăng cường vận động. Nên tham khảo ý kiến chuyên gia dinh dưỡng nếu cần.";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resultCardView.setVisibility(View.GONE);
    }

    private void resetForm() {
        editTextAge.setText("");
        editTextHeight.setText("");
        editTextWeight.setText("");
        radioGroupGender.clearCheck();
        resultCardView.setVisibility(View.GONE);
    }
} 