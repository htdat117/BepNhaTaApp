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

import java.text.Normalizer;
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
                .inflate(R.layout.item_recipe_in_list, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeItem currentItem = recipeList.get(position);
        // Load ảnh món ăn
        if (currentItem.getImageData() != null && currentItem.getImageData().length > 0) {
            Glide.with(holder.imvRecipe.getContext())
                .load(currentItem.getImageData())
                .placeholder(R.drawable.placeholder_banner_background)
                .into(holder.imvRecipe);
        } else if (currentItem.getImageUrl() != null && !currentItem.getImageUrl().isEmpty()) {
            String url = currentItem.getImageUrl();
            if (url.startsWith("http")) {
                Glide.with(holder.imvRecipe.getContext())
                    .load(url)
                    .placeholder(R.drawable.placeholder_banner_background)
                    .into(holder.imvRecipe);
            } else {
                int resId = holder.imvRecipe.getContext().getResources().getIdentifier(url, "drawable", holder.imvRecipe.getContext().getPackageName());
                holder.imvRecipe.setImageResource(resId != 0 ? resId : R.drawable.placeholder_banner_background);
            }
        } else {
            holder.imvRecipe.setImageResource(currentItem.getImageResId());
        }
        // Tên món
        holder.txtName.setText(currentItem.getName());
        // Thời gian nấu
        holder.txtTime.setText(currentItem.getTime());
        // Độ khó
        holder.txtLevel.setText(currentItem.getLevel());
        // Lợi ích sức khỏe / dinh dưỡng
        if (holder.txtBenefit != null) {
            holder.txtBenefit.setText(currentItem.getBenefit());
        }

        // Đặt icon lợi ích
        ImageView imvUse = holder.itemView.findViewById(R.id.imvUse);
        if(imvUse != null && currentItem.getBenefit()!=null){
            String slug = slugify(currentItem.getBenefit());
            int res = holder.itemView.getResources().getIdentifier("ic_"+slug, "drawable", holder.itemView.getContext().getPackageName());
            if(res==0){
                if(slug.contains("ngu")) slug="sleepy";
                else if(slug.contains("skin")||slug.contains("da")) slug="skin";
                else if(slug.contains("xuong")) slug="bone";
                else if(slug.contains("bo_mau")|| (slug.contains("mau")&&!slug.contains("bo_mau"))) slug="blood";
                else if(slug.contains("giai_doc")||slug.contains("detox")||slug.contains("doc")) slug="detox";
                else if(slug.contains("giam_can")||slug.contains("weight")) slug="weight";
                else if(slug.contains("tim")||slug.contains("heart")) slug="tim";
                res = holder.itemView.getResources().getIdentifier("ic_"+slug, "drawable", holder.itemView.getContext().getPackageName());
                if(res==0) res = R.drawable.ic_bone;
            }
            imvUse.setImageResource(res);
        }
        // Số lượt thích
        holder.txtLove.setText(String.valueOf(currentItem.getLikeCount()));
        // Số bình luận
        holder.txtComment.setText(String.valueOf(currentItem.getCommentCount()));
        // Gán sự kiện click vào toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onView(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    private String slugify(String input){
        if(input==null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        temp = temp.toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9]+","_");
        if(temp.startsWith("_")) temp = temp.substring(1);
        if(temp.endsWith("_")) temp = temp.substring(0, temp.length()-1);
        return temp;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvRecipe;
        public TextView txtName, txtTime, txtLevel, txtBenefit, txtLove, txtComment;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imvRecipe = itemView.findViewById(R.id.imvRecipe);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtLevel = itemView.findViewById(R.id.txtLevel);
            txtBenefit = itemView.findViewById(R.id.txtBenefit);
            txtLove = itemView.findViewById(R.id.txtLove);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }
} 