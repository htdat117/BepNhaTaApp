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
        // Độ khó (giả lập)
        holder.txtLevel.setText(currentItem.getProtein()); // Tạm dùng trường protein làm độ khó
        // Số lượt thích
        holder.txtLove.setText("150"); // Nếu có trường like thì truyền vào
        // Số bình luận
        holder.txtComment.setText("110"); // Nếu có trường comment thì truyền vào
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

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvRecipe;
        public TextView txtName, txtTime, txtLevel, txtLove, txtComment;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imvRecipe = itemView.findViewById(R.id.imvRecipe);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtLevel = itemView.findViewById(R.id.txtLevel);
            txtLove = itemView.findViewById(R.id.txtLove);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }
} 