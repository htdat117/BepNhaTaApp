package com.example.bepnhataapp.features.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<RecipeItem> recipeList;
    private OnRecipeActionListener listener;

    public interface OnRecipeActionListener {
        void onView(RecipeItem recipe);
        void onDelete(RecipeItem recipe);
    }

    public RecipeAdapter(List<RecipeItem> recipeList, OnRecipeActionListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_downloaded_recipe, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeItem currentItem = recipeList.get(position);

        holder.imageViewRecipe.setImageResource(currentItem.getImageResId());
        holder.textViewRecipeName.setText(currentItem.getName());
        holder.textViewCalories.setText(currentItem.getCalories());
        holder.textViewProtein.setText(currentItem.getProtein());
        holder.textViewTime.setText(currentItem.getTime());

        holder.btnDeleteRecipe.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(currentItem);
            }
        });

        holder.btnViewRecipe.setOnClickListener(v -> {
            if (listener != null) {
                listener.onView(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewRecipe;
        public TextView textViewRecipeName;
        public TextView textViewCalories;
        public TextView textViewProtein;
        public TextView textViewTime;
        public Button btnDeleteRecipe;
        public Button btnViewRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageViewRecipe);
            textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
            textViewCalories = itemView.findViewById(R.id.textViewCalories);
            textViewProtein = itemView.findViewById(R.id.textViewProtein);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            btnDeleteRecipe = itemView.findViewById(R.id.btnDeleteRecipe);
            btnViewRecipe = itemView.findViewById(R.id.btnViewRecipe);
        }
    }
} 