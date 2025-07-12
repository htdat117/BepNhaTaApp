package com.example.bepnhataapp.common.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.DownloadedRecipe;
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
        Log.d("DownloadedRecipeAdapter", "Bind: title=" + recipe.getTitle() + ", time=" + recipe.getTime() + ", benefit=" + recipe.getBenefit() + ", level=" + recipe.getLevel());
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
        // Tag thời gian
        if (recipe.getTime() != null && !recipe.getTime().trim().isEmpty()) {
            holder.layoutTagTime.setVisibility(View.VISIBLE);
            holder.tvTagTime.setText(recipe.getTime());
        } else {
            holder.layoutTagTime.setVisibility(View.GONE);
        }
        // Tag benefit
        if (recipe.getBenefit() != null && !recipe.getBenefit().trim().isEmpty()) {
            holder.layoutTagBenefit.setVisibility(View.VISIBLE);
            holder.tvTagBenefit.setText(recipe.getBenefit());
            // Mapping icon động giống online
            String slug = slugify(recipe.getBenefit());
            int res = holder.imgTagBenefit.getContext().getResources().getIdentifier("ic_"+slug, "drawable", holder.imgTagBenefit.getContext().getPackageName());
            if(res == 0) {
                if(slug.contains("ngu")) slug = "sleepy";
                else if(slug.contains("skin") || slug.contains("da")) slug = "skin";
                else if(slug.contains("xuong")) slug = "bone";
                else if(slug.contains("bo_mau") || (slug.contains("mau") && !slug.contains("bo_mau"))) slug = "blood";
                else if(slug.contains("giai_doc") || slug.contains("detox") || slug.contains("doc")) slug = "detox";
                else if(slug.contains("giam_can") || slug.contains("weight")) slug = "weight";
                else if(slug.contains("tim") || slug.contains("heart")) slug = "tim";
                res = holder.imgTagBenefit.getContext().getResources().getIdentifier("ic_"+slug, "drawable", holder.imgTagBenefit.getContext().getPackageName());
                if(res == 0) res = R.drawable.ic_bone;
            }
            holder.imgTagBenefit.setImageResource(res);
        } else {
            holder.layoutTagBenefit.setVisibility(View.GONE);
        }
        // Tag level
        if (recipe.getLevel() != null && !recipe.getLevel().trim().isEmpty()) {
            holder.layoutTagLevel.setVisibility(View.VISIBLE);
            holder.tvTagLevel.setText(recipe.getLevel());
            // Mapping icon động cho level nếu cần (ở đây giữ nguyên ic_medium_time)
            holder.imgTagLevel.setImageResource(R.drawable.ic_medium_time);
        } else {
            holder.layoutTagLevel.setVisibility(View.GONE);
        }
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(recipe));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecipeDetailOfflineActivity.class);
            intent.putExtra("recipeId", recipe.getRecipeId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    // Hàm slugify giống online
    private String slugify(String input){
        if(input==null) return "";
        String temp = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        temp = temp.replaceAll("đ", "d").replaceAll("Đ", "d");
        temp = temp.toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9]+","_");
        if(temp.startsWith("_")) temp = temp.substring(1);
        if(temp.endsWith("_")) temp = temp.substring(0, temp.length()-1);
        return temp;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRecipe;
        TextView tvTitle;
        LinearLayout layoutTagTime, layoutTagBenefit, layoutTagLevel;
        ImageView imgTagTime, imgTagBenefit, imgTagLevel;
        TextView tvTagTime, tvTagBenefit, tvTagLevel;
        Button btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imageViewRecipe);
            tvTitle = itemView.findViewById(R.id.textViewRecipeName);
            layoutTagTime = itemView.findViewById(R.id.layoutTagTime);
            layoutTagBenefit = itemView.findViewById(R.id.layoutTagBenefit);
            layoutTagLevel = itemView.findViewById(R.id.layoutTagLevel);
            imgTagTime = itemView.findViewById(R.id.imgTagTime);
            imgTagBenefit = itemView.findViewById(R.id.imgTagBenefit);
            imgTagLevel = itemView.findViewById(R.id.imgTagLevel);
            tvTagTime = itemView.findViewById(R.id.tvTagTime);
            tvTagBenefit = itemView.findViewById(R.id.tvTagBenefit);
            tvTagLevel = itemView.findViewById(R.id.tvTagLevel);
            btnDelete = itemView.findViewById(R.id.btnDeleteRecipe);
        }
    }
} 
