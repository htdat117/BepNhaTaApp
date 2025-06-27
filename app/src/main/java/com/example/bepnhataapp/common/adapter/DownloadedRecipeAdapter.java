package com.example.bepnhataapp.common.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.DownloadedRecipe;
import com.example.bepnhataapp.features.offline.RecipeDetailOfflineActivity;
import java.util.List;

public class DownloadedRecipeAdapter extends RecyclerView.Adapter<DownloadedRecipeAdapter.ViewHolder> {
    private List<DownloadedRecipe> list;
    private OnRecipeActionListener listener;

    public interface OnRecipeActionListener {
        void onView(DownloadedRecipe recipe);
        void onDelete(DownloadedRecipe recipe);
    }

    public DownloadedRecipeAdapter(List<DownloadedRecipe> list, OnRecipeActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloaded_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadedRecipe recipe = list.get(position);
        if (recipe.getImageThumb() != null && !recipe.getImageThumb().trim().isEmpty()) {
            String imgStr = recipe.getImageThumb().trim();
            if (imgStr.startsWith("http")) {
                Glide.with(holder.imgRecipe.getContext())
                        .load(imgStr)
                        .placeholder(R.drawable.food_placeholder)
                        .into(holder.imgRecipe);
            } else {
                int resId = holder.imgRecipe.getContext().getResources().getIdentifier(imgStr, "drawable", holder.imgRecipe.getContext().getPackageName());
                if (resId != 0) {
                    holder.imgRecipe.setImageResource(resId);
                } else {
                    holder.imgRecipe.setImageResource(R.drawable.food_placeholder);
                }
            }
        } else {
            holder.imgRecipe.setImageResource(recipe.getImageResId());
        }
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvCal.setText(recipe.getCal());
        holder.tvType.setText(recipe.getType());
        holder.tvTime.setText(recipe.getTime());
        holder.btnView.setOnClickListener(v -> {
            if (v.getContext() != null) {
                Intent intent = new Intent(v.getContext(), RecipeDetailOfflineActivity.class);
                intent.putExtra("recipeId", recipe.getRecipeId());
                v.getContext().startActivity(intent);
            }
            if (listener != null) listener.onView(recipe);
        });
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(recipe));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRecipe;
        TextView tvTitle, tvCal, tvType, tvTime;
        Button btnView, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imageViewRecipe);
            tvTitle = itemView.findViewById(R.id.textViewRecipeName);
            tvCal = itemView.findViewById(R.id.textViewCalories);
            tvType = itemView.findViewById(R.id.textViewProtein);
            tvTime = itemView.findViewById(R.id.textViewTime);
            btnView = itemView.findViewById(R.id.btnViewRecipe);
            btnDelete = itemView.findViewById(R.id.btnDeleteRecipe);
        }
    }
} 