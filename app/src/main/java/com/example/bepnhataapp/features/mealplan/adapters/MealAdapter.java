package com.example.bepnhataapp.features.mealplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealVH> {

    public static class MealRow {
        public final String name;
        public final int imageRes;
        public MealRow(String n, int img) { name = n; imageRes = img; }
    }

    private final List<MealRow> meals;
    public MealAdapter(List<MealRow> list) { this.meals = list; }

    @NonNull
    @Override
    public MealVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_row, parent, false);
        return new MealVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealVH holder, int position) {
        MealRow row = meals.get(position);
        holder.tvName.setText(row.name);
        holder.img.setImageResource(row.imageRes);
    }

    @Override
    public int getItemCount() { return meals.size(); }

    static class MealVH extends RecyclerView.ViewHolder {
        ImageView img; TextView tvName;
        MealVH(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.ivMeal);
            tvName = itemView.findViewById(R.id.tvMealName);
        }
    }
} 