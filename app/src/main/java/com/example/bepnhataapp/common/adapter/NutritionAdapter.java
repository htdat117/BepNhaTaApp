package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import java.util.List;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.NutritionViewHolder> {
    private Context context;
    private List<NutritionItem> list;

    public NutritionAdapter(Context context, List<NutritionItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NutritionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nutrition, parent, false);
        return new NutritionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NutritionViewHolder holder, int position) {
        NutritionItem item = list.get(position);
        holder.ivIcon.setImageResource(item.iconResId);
        holder.tvName.setText(item.name);
        holder.tvValue.setText(item.value);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NutritionItem {
        public String name;
        public String value;
        public int iconResId;
        public NutritionItem(String name, String value, int iconResId) {
            this.name = name;
            this.value = value;
            this.iconResId = iconResId;
        }
    }

    static class NutritionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvValue;
        public NutritionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivNutritionIcon);
            tvName = itemView.findViewById(R.id.tvNutritionName);
            tvValue = itemView.findViewById(R.id.tvNutritionValue);
        }
    }
} 
