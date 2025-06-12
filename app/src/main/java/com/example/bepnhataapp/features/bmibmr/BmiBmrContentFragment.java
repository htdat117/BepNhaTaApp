package com.example.bepnhataapp.features.bmibmr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.example.bepnhataapp.R;

public class BmiBmrContentFragment extends Fragment {
    private static final String TAG = "BmiBmrContentFragment";

    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale;
    private EditText editTextAge, editTextHeight, editTextWeight;
    private Button btnCalculateBmi, btnCalculateBmr, btnReset;
    private TextView textViewBmiResult, textViewBmrResult, textViewBmiCategory, textViewAdvice, textViewResultTitle;
    private View resultCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating fragment view");
        View view = inflater.inflate(R.layout.fragment_bmi_bmr_content, container, false);
        
        // Thêm log kiểm tra view debugText



        // Ánh xạ các View
        try {
            radioGroupGender = view.findViewById(R.id.radioGroupGender);
            Log.d(TAG, "radioGroupGender: " + (radioGroupGender != null ? "Found" : "Null"));
            radioMale = view.findViewById(R.id.radioMale);
            Log.d(TAG, "radioMale: " + (radioMale != null ? "Found" : "Null"));
            radioFemale = view.findViewById(R.id.radioFemale);
            Log.d(TAG, "radioFemale: " + (radioFemale != null ? "Found" : "Null"));
            editTextAge = view.findViewById(R.id.editTextAge);
            Log.d(TAG, "editTextAge: " + (editTextAge != null ? "Found" : "Null"));
            editTextHeight = view.findViewById(R.id.editTextHeight);
            Log.d(TAG, "editTextHeight: " + (editTextHeight != null ? "Found" : "Null"));
            editTextWeight = view.findViewById(R.id.editTextWeight);
            Log.d(TAG, "editTextWeight: " + (editTextWeight != null ? "Found" : "Null"));
            btnCalculateBmi = view.findViewById(R.id.btnCalculateBmi);
            Log.d(TAG, "btnCalculateBmi: " + (btnCalculateBmi != null ? "Found" : "Null"));
            btnCalculateBmr = view.findViewById(R.id.btnCalculateBmr);
            Log.d(TAG, "btnCalculateBmr: " + (btnCalculateBmr != null ? "Found" : "Null"));
            btnReset = view.findViewById(R.id.btnReset);
            Log.d(TAG, "btnReset: " + (btnReset != null ? "Found" : "Null"));
            textViewBmiResult = view.findViewById(R.id.textViewBmiResult);
            Log.d(TAG, "textViewBmiResult: " + (textViewBmiResult != null ? "Found" : "Null"));
            textViewBmrResult = view.findViewById(R.id.textViewBmrResult);
            Log.d(TAG, "textViewBmrResult: " + (textViewBmrResult != null ? "Found" : "Null"));
            textViewBmiCategory = view.findViewById(R.id.textViewBmiCategory);
            Log.d(TAG, "textViewBmiCategory: " + (textViewBmiCategory != null ? "Found" : "Null"));
            textViewAdvice = view.findViewById(R.id.textViewAdvice);
            Log.d(TAG, "textViewAdvice: " + (textViewAdvice != null ? "Found" : "Null"));
            textViewResultTitle = view.findViewById(R.id.textViewResultTitle);
            Log.d(TAG, "textViewResultTitle: " + (textViewResultTitle != null ? "Found" : "Null"));
            resultCardView = view.findViewById(R.id.resultCardView);
            Log.d(TAG, "resultCardView: " + (resultCardView != null ? "Found" : "Null"));

            // Kiểm tra xem tất cả các view đã được tìm thấy chưa
            if (radioGroupGender == null || radioMale == null || radioFemale == null ||
                editTextAge == null || editTextHeight == null || editTextWeight == null ||
                btnCalculateBmi == null || btnCalculateBmr == null || btnReset == null ||
                textViewBmiResult == null || textViewBmrResult == null || textViewBmiCategory == null ||
                textViewAdvice == null || textViewResultTitle == null || resultCardView == null) {
                Log.e(TAG, "onCreateView: Some views were not found");
                return view;
            }

            Log.d(TAG, "onCreateView: All views found successfully");

            // Gán sự kiện click cho các nút
            btnCalculateBmi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Calculate BMI button clicked");
                    if (validateInputs()) {
                        calculateAndShowResults(true);
                    }
                }
            });

            btnCalculateBmr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Calculate BMR button clicked");
                    if (validateInputs()) {
                        calculateAndShowResults(false);
                    }
                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Reset button clicked");
                    resetForm();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Error setting up views", e);
        }

        return view;
    }

    private boolean validateInputs() {
        if (editTextAge.getText().toString().isEmpty() ||
            editTextHeight.getText().toString().isEmpty() ||
            editTextWeight.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!radioMale.isChecked() && !radioFemale.isChecked()) {
            Toast.makeText(getContext(), "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int age = Integer.parseInt(editTextAge.getText().toString());
            double height = Double.parseDouble(editTextHeight.getText().toString());
            double weight = Double.parseDouble(editTextWeight.getText().toString());

            if (age <= 0 || age > 120) {
                Toast.makeText(getContext(), "Tuổi không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (height <= 0 || height > 300) {
                Toast.makeText(getContext(), "Chiều cao không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (weight <= 0 || weight > 500) {
                Toast.makeText(getContext(), "Cân nặng không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void calculateAndShowResults(boolean isBmiCalculation) {
        int age = Integer.parseInt(editTextAge.getText().toString());
        double heightCm = Double.parseDouble(editTextHeight.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        double heightM = heightCm / 100;
        
        double bmi = calculateBMI(heightM, weight);
        String bmiCategory = getBMICategory(bmi);
        double bmr = calculateBMR(age, heightCm, weight, radioMale.isChecked());
        String advice = getAdvice(bmi);
        
        showResult(bmi, bmiCategory, bmr, advice);
        
        // Hiển thị thông báo tương ứng
        Toast.makeText(getContext(), 
            isBmiCalculation ? "Đã tính xong chỉ số BMI" : "Đã tính xong chỉ số BMR", 
            Toast.LENGTH_SHORT).show();
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
        if (isMale) {
            return (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            return (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
    }

    private void showResult(double bmi, String bmiCategory, double bmr, String advice) {
        resultCardView.setAlpha(0f);
        resultCardView.setVisibility(View.VISIBLE);
        resultCardView.animate()
                .alpha(1f)
                .setDuration(500)
                .start();

        textViewResultTitle.setText("Kết quả tính toán");
        textViewBmiResult.setText(String.format("BMI của bạn là: %.1f", bmi));
        textViewBmiCategory.setText(String.format("Phân loại: %s", bmiCategory));
        textViewBmrResult.setText(String.format("BMR của bạn là: %.0f kcal/ngày", bmr));
        
        String bmrDetail = String.format(
            "BMR (Basal Metabolic Rate) là lượng calo cơ thể đốt cháy khi nghỉ ngơi.\n\n" +
            "Dựa vào BMR, bạn có thể:\n" +
            "- Nhân với 1.2: Ít vận động\n" +
            "- Nhân với 1.375: Vận động nhẹ (1-3 lần/tuần)\n" +
            "- Nhân với 1.55: Vận động vừa (3-5 lần/tuần)\n" +
            "- Nhân với 1.725: Vận động nhiều (6-7 lần/tuần)\n" +
            "- Nhân với 1.9: Vận động rất nhiều (2 lần/ngày)\n\n" +
            "Kết quả sẽ cho biết tổng lượng calo bạn cần mỗi ngày."
        );
        textViewAdvice.setText(String.format("Lời khuyên: %s\n\n%s", advice, bmrDetail));
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

    private void resetForm() {
        editTextAge.setText("");
        editTextHeight.setText("");
        editTextWeight.setText("");
        radioGroupGender.clearCheck();
        resultCardView.setVisibility(View.GONE);
    }
} 