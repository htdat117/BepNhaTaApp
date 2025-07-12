package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class MealTimeListAdapter extends RecyclerView.Adapter<MealTimeListAdapter.MTViewHolder> {

    public static class MealTimeWithMeals {
        public final String label;
        public final List<MealAdapter.MealRow> meals;
        public MealTimeWithMeals(String l, List<MealAdapter.MealRow> m){ label = l; meals = m; }
    }

    private final List<MealTimeWithMeals> items;
    public MealTimeListAdapter(List<MealTimeWithMeals> data){ this.items = data; }

    @NonNull
    @Override
    public MTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_time, parent, false);
        return new MTViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MTViewHolder holder, int position) {
        MealTimeWithMeals mt = items.get(position);
        holder.tvLabel.setText(mt.label);
        MealAdapter adapter = new MealAdapter(mt.meals);
        holder.rvMeals.setAdapter(adapter);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class MTViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel; RecyclerView rvMeals;
        MTViewHolder(@NonNull View itemView){
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvMealTimeLabel);
            rvMeals = itemView.findViewById(R.id.rvMeals);
            rvMeals.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
} 
