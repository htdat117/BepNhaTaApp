package com.example.bepnhataapp.features.calocal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.bmibmr.BmiBmrCalculatorActivity;

public class CaloCalculatorActivity extends AppCompatActivity {

    private EditText editTextFoodName, editTextWeight;
    private Button btnSave, btnDelete, btnCalculate;
    private Button btnBmiBmrTab, btnCaloTab;
    private ListView listViewFoods;
    private TextView textViewTotalCalories;
    
    private ArrayList<String> foodList;
    private ArrayAdapter<String> adapter;
    private Map<String, Double> foodCaloriesMap; // Map lưu calo/100g cho mỗi món ăn
    private double totalCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calo_calculator);

        // Ánh xạ các View từ layout
        editTextFoodName = findViewById(R.id.editTextFoodName);
        editTextWeight = findViewById(R.id.editTextWeight);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnCalculate = findViewById(R.id.btnCalculateBmi);
        btnBmiBmrTab = findViewById(R.id.btnBmiBmrTab);
        btnCaloTab = findViewById(R.id.btnCaloTab);
        listViewFoods = findViewById(R.id.listViewFoods);
        textViewTotalCalories = findViewById(R.id.textViewTotalCalories);

        // Khởi tạo dữ liệu
        foodList = new ArrayList<>();
        foodCaloriesMap = new HashMap<>();
        initializeFoodCalories();
        
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodList);
        listViewFoods.setAdapter(adapter);

        // Xử lý sự kiện click cho nút Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();
            }
        });

        // Xử lý sự kiện click cho nút Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedFood();
            }
        });

        // Xử lý sự kiện click cho nút Calculate
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });

        // Xử lý sự kiện click cho nút BMI/BMR Tab
        btnBmiBmrTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaloCalculatorActivity.this, BmiBmrCalculatorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện click cho item trong ListView
        listViewFoods.setOnItemClickListener((parent, view, position, id) -> {
            String selectedFood = foodList.get(position);
            showFoodDetails(selectedFood);
        });
    }

    private void initializeFoodCalories() {
        // Thêm một số món ăn mẫu với lượng calo/100g
        foodCaloriesMap.put("Cơm trắng", 130.0);
        foodCaloriesMap.put("Thịt heo", 242.0);
        foodCaloriesMap.put("Thịt bò", 250.0);
        foodCaloriesMap.put("Cá hồi", 208.0);
        foodCaloriesMap.put("Rau xanh", 20.0);
        foodCaloriesMap.put("Trứng gà", 155.0);
        foodCaloriesMap.put("Sữa tươi", 42.0);
        foodCaloriesMap.put("Chuối", 89.0);
        foodCaloriesMap.put("Táo", 52.0);
        foodCaloriesMap.put("Bánh mì", 265.0);
    }

    private void saveFood() {
        String foodName = editTextFoodName.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();

        if (foodName.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                Toast.makeText(this, "Khối lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem món ăn có trong danh sách không
            if (!foodCaloriesMap.containsKey(foodName)) {
                showAddNewFoodDialog(foodName, weight);
            } else {
                String foodEntry = String.format("%s - %.1fg", foodName, weight);
                foodList.add(foodEntry);
                adapter.notifyDataSetChanged();
                calculateTotalCalories();
                clearInputs();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddNewFoodDialog(String foodName, double weight) {
        new AlertDialog.Builder(this)
            .setTitle("Thêm món ăn mới")
            .setMessage("Món ăn này chưa có trong danh sách. Bạn có muốn thêm không?")
            .setPositiveButton("Thêm", (dialog, which) -> {
                showCaloriesInputDialog(foodName, weight);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showCaloriesInputDialog(String foodName, double weight) {
        EditText input = new EditText(this);
        input.setHint("Nhập calo/100g");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
            .setTitle("Nhập thông tin calo")
            .setView(input)
            .setPositiveButton("Lưu", (dialog, which) -> {
                try {
                    double calories = Double.parseDouble(input.getText().toString());
                    if (calories > 0) {
                        foodCaloriesMap.put(foodName, calories);
                        String foodEntry = String.format("%s - %.1fg", foodName, weight);
                        foodList.add(foodEntry);
                        adapter.notifyDataSetChanged();
                        calculateTotalCalories();
                        clearInputs();
                    } else {
                        Toast.makeText(this, "Calo phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteSelectedFood() {
        int position = listViewFoods.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            foodList.remove(position);
            adapter.notifyDataSetChanged();
            calculateTotalCalories();
        } else {
            Toast.makeText(this, "Vui lòng chọn món ăn cần xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateCalories() {
        calculateTotalCalories();
        String message = String.format("Tổng calo: %.1f kcal", totalCalories);
        textViewTotalCalories.setText(message);
    }

    private void calculateTotalCalories() {
        totalCalories = 0;
        for (String foodEntry : foodList) {
            String[] parts = foodEntry.split(" - ");
            String foodName = parts[0];
            double weight = Double.parseDouble(parts[1].replace("g", ""));
            double caloriesPer100g = foodCaloriesMap.get(foodName);
            totalCalories += (weight * caloriesPer100g) / 100;
        }
        textViewTotalCalories.setText(String.format("Tổng calo: %.1f kcal", totalCalories));
    }

    private void showFoodDetails(String foodEntry) {
        String[] parts = foodEntry.split(" - ");
        String foodName = parts[0];
        double weight = Double.parseDouble(parts[1].replace("g", ""));
        double caloriesPer100g = foodCaloriesMap.get(foodName);
        double totalCalories = (weight * caloriesPer100g) / 100;

        String message = String.format("Món: %s\nKhối lượng: %.1fg\nCalo/100g: %.1f kcal\nTổng calo: %.1f kcal",
                foodName, weight, caloriesPer100g, totalCalories);

        new AlertDialog.Builder(this)
            .setTitle("Chi tiết món ăn")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private void clearInputs() {
        editTextFoodName.setText("");
        editTextWeight.setText("");
    }
} 