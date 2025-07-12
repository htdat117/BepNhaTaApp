package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> list;
    private int layoutId = R.layout.item_ingredient_grid;

    public IngredientAdapter(Context context, List<Ingredient> list) {
        this(context, list, R.layout.item_ingredient_grid);
    }

    public IngredientAdapter(Context context, List<Ingredient> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ing = list.get(position);
        if (ing.imageUrl != null && !ing.imageUrl.isEmpty()) {
            String url = ing.imageUrl.startsWith("@") ? ing.imageUrl.substring(1) : ing.imageUrl;
            Glide.with(context).load(url).placeholder(R.drawable.food_placeholder).into(holder.ivIcon);
        } else if (ing.image != null && ing.image.length > 0) {
            Glide.with(context).load(ing.image).placeholder(R.drawable.food_placeholder).into(holder.ivIcon);
        } else if (ing.imageResId != 0) {
            holder.ivIcon.setImageResource(ing.imageResId);
        } else {
            holder.ivIcon.setImageResource(R.drawable.food_placeholder); // fallback icon
        }
        holder.tvName.setText(ing.ingredientName);
        holder.tvQuantity.setText(ing.quantity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<Ingredient> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvQuantity;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIngredient);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvIngredientQuantity);
        }
    }
} 
