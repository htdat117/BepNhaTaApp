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
import android.util.Log;

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
        Log.d("DEBUG_STEP", "Step: " + step.getTitle() + ", image: " + step.getImage());
        holder.tvStepTitle.setText(step.getTitle());
        holder.tvStepContent.setText(step.getContent());
        holder.ivStepImage.setVisibility(View.VISIBLE);
        if (step.getImage() != null && !step.getImage().isEmpty() && (step.getImage().startsWith("http://") || step.getImage().startsWith("https://"))) {
            Glide.with(holder.ivStepImage.getContext())
                .load(step.getImage())
                .placeholder(R.drawable.food_placeholder)
                .into(holder.ivStepImage);
        } else {
            holder.ivStepImage.setImageResource(R.drawable.food_placeholder);
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