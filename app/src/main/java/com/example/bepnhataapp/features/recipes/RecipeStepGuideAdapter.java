package com.example.bepnhataapp.features.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.InstructionRecipe;
import java.util.List;

public class RecipeStepGuideAdapter extends RecyclerView.Adapter<RecipeStepGuideAdapter.StepViewHolder> {
    private List<InstructionRecipe> steps;

    public RecipeStepGuideAdapter(List<InstructionRecipe> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_step_guide, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        InstructionRecipe step = steps.get(position);
        holder.tvStepTitle.setText(step.getTitle());
        holder.tvStepContent.setText(step.getContent());
        if (step.getImage() != null && !step.getImage().isEmpty()) {
            Glide.with(holder.ivStepImage.getContext())
                .load(step.getImage())
                .placeholder(R.drawable.food_placeholder)
                .into(holder.ivStepImage);
            holder.ivStepImage.setVisibility(View.VISIBLE);
        } else {
            holder.ivStepImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView tvStepTitle, tvStepContent;
        ImageView ivStepImage;
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStepTitle = itemView.findViewById(R.id.txtTitle);
            tvStepContent = itemView.findViewById(R.id.txtContent);
            ivStepImage = itemView.findViewById(R.id.imvStep);
        }
    }
} 