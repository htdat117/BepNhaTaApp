package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealVH> {

    public static class MealRow {
        public final String name;
        public final int imageRes;
        public final String imageUrl;

        public MealRow(String n, int img) { this(n, img, null); }
        public MealRow(String n, String url) { this(n, 0, url); }
        private MealRow(String n, int res, String url) {
            name = n;
            imageRes = res;
            imageUrl = url;
        }
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
        if(row.imageUrl != null && !row.imageUrl.isEmpty()){
            Glide.with(holder.img.getContext())
                    .load(row.imageUrl)
                    .placeholder(row.imageRes == 0 ? com.example.bepnhataapp.R.drawable.placeholder_banner_background : row.imageRes)
                    .into(holder.img);
        }else{
            holder.img.setImageResource(row.imageRes);
        }
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
