package com.example.bepnhataapp.features.calocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.FoodItemAdapter;

public class CaloCalculatorFragment extends Fragment {

    private static final String TAG = "CaloCalculatorFragment";

    private EditText editTextFoodName, editTextWeight;
    private Button btnSave, btnDelete;
    private ListView listViewFoods;
    private TextView textViewTotalCalories;

    private ArrayList<FoodItem> foodList;
    private FoodItemAdapter adapter;
    private Map<String, FoodData> foodCaloriesMap;
    private double totalCalories = 0;

    public static class FoodData {
        double caloriesPer100g;
        int imageResId;

        public FoodData(double caloriesPer100g, int imageResId) {
            this.caloriesPer100g = caloriesPer100g;
            this.imageResId = imageResId;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating fragment view");
        View view = inflater.inflate(R.layout.fragment_calo_calculator, container, false);

        editTextFoodName = view.findViewById(R.id.editTextFoodName);
        Log.d(TAG, "editTextFoodName: " + (editTextFoodName != null ? "Found" : "Null"));
        editTextWeight = view.findViewById(R.id.editTextWeight);
        Log.d(TAG, "editTextWeight: " + (editTextWeight != null ? "Found" : "Null"));
        btnSave = view.findViewById(R.id.btnSave);
        Log.d(TAG, "btnSave: " + (btnSave != null ? "Found" : "Null"));
        btnDelete = view.findViewById(R.id.btnDelete);
        Log.d(TAG, "btnDelete: " + (btnDelete != null ? "Found" : "Null"));
        listViewFoods = view.findViewById(R.id.listViewFoods);
        Log.d(TAG, "listViewFoods: " + (listViewFoods != null ? "Found" : "Null"));
        textViewTotalCalories = view.findViewById(R.id.textViewTotalCalories);
        Log.d(TAG, "textViewTotalCalories: " + (textViewTotalCalories != null ? "Found" : "Null"));

        foodList = new ArrayList<>();
        foodCaloriesMap = new HashMap<>();
        initializeFoodCalories();

        adapter = new FoodItemAdapter(getContext(), R.layout.item_food_history, foodList);
        listViewFoods.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save button clicked");
                saveFood();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Delete button clicked");
                deleteSelectedFood();
            }
        });

        listViewFoods.setOnItemClickListener((parent, view1, position, id) -> {
            FoodItem selectedFood = foodList.get(position);
            showFoodDetails(selectedFood);
        });

        return view;
    }

    private void initializeFoodCalories() {
        foodCaloriesMap.put("Cơm trắng", new FoodData(130.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Thịt heo", new FoodData(242.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Thịt bò", new FoodData(250.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Cá hồi", new FoodData(208.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Rau xanh", new FoodData(20.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Trứng gà", new FoodData(155.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Sữa tươi", new FoodData(42.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Chuối", new FoodData(89.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Táo", new FoodData(52.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Bánh mì", new FoodData(265.0, R.drawable.food_placeholder));

        foodCaloriesMap.put("Cua biển tươi", new FoodData(60.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Xương heo", new FoodData(120.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Tôm tươi", new FoodData(99.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Chả cua", new FoodData(170.0, R.drawable.food_placeholder));
        foodCaloriesMap.put("Bánh canh", new FoodData(110.0, R.drawable.food_placeholder));
    }

    private void addSampleFoodItem(String foodName, double weight) {
        FoodData data = foodCaloriesMap.get(foodName);
        if (data != null) {
            double calories = weight * data.caloriesPer100g / 100.0;
            foodList.add(new FoodItem(foodName, weight, calories, data.imageResId));
        }
    }

    private void saveFood() {
        String foodName = editTextFoodName.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();

        if (foodName.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                Toast.makeText(getContext(), "Khối lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            FoodData foodData = foodCaloriesMap.get(foodName);

            if (foodData == null) {
                Toast.makeText(getContext(), "Món ăn chưa có trong danh sách!", Toast.LENGTH_SHORT).show();
            } else {
                double calories = weight * foodData.caloriesPer100g / 100.0;
                foodList.add(new FoodItem(foodName, weight, calories, foodData.imageResId));
                adapter.notifyDataSetChanged();
                calculateTotalCalories();
                clearInputs();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSelectedFood() {
        int position = listViewFoods.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            foodList.remove(position);
            adapter.notifyDataSetChanged();
            calculateTotalCalories();
        } else {
            Toast.makeText(getContext(), "Vui lòng chọn món ăn cần xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateTotalCalories() {
        totalCalories = 0;
        for (FoodItem item : foodList) {
            totalCalories += item.getCalories();
        }
        textViewTotalCalories.setText(String.format("Tổng calo: %.1f kcal", totalCalories));
    }

    private void showFoodDetails(FoodItem foodItem) {
        String message = String.format(
            "Món: %s\nKhối lượng: %.1fg\nCalo/100g: %.1f kcal\nTổng calo: %.1f kcal",
            foodItem.getName(),
            foodItem.getWeight(),
            foodCaloriesMap.get(foodItem.getName()).caloriesPer100g,
            foodItem.getCalories()
        );

        new AlertDialog.Builder(getContext())
            .setTitle("Chi tiết món ăn")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private void clearInputs() {
        editTextFoodName.setText("");
        editTextWeight.setText("");
        editTextFoodName.requestFocus();
    }
} 