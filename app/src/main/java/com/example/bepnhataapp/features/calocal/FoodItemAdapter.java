package com.example.bepnhataapp.features.calocal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bepnhataapp.R;

import java.util.List;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {

    private Context context;
    private List<FoodItem> foodList;

    public FoodItemAdapter(Context context, int resource, List<FoodItem> foodList) {
        super(context, resource, foodList);
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_food_history, parent, false);
        }

        FoodItem foodItem = foodList.get(position);

        ImageView imageViewFood = convertView.findViewById(R.id.imageViewFood);
        TextView textViewFoodName = convertView.findViewById(R.id.textViewFoodName);
        TextView textViewWeight = convertView.findViewById(R.id.textViewWeight);
        TextView textViewCalories = convertView.findViewById(R.id.textViewCalories);

        imageViewFood.setImageResource(foodItem.getImageResId());
        textViewFoodName.setText(foodItem.getName());
        textViewWeight.setText(String.format("Khối lượng: %.1fg", foodItem.getWeight()));
        textViewCalories.setText(String.format("Calo: %.1f kcal", foodItem.getCalories()));

        return convertView;
    }
} 