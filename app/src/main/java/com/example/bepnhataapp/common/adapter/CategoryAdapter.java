package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.common.model.Category;
import com.example.bepnhataapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private final List<Category> categoryList;
    private int selectedPos = 0;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener l) { this.listener = l; }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.imgCategory.setImageResource(category.iconResId);
        holder.tvCategoryName.setText(category.name);

        boolean selected = (position == selectedPos);
        holder.imgCategory.setBackgroundResource(selected ? R.drawable.bg_category_selected : R.drawable.bg_rounded_8);
        holder.tvCategoryName.setTextColor(context.getResources().getColor(selected ? R.color.primary1 : R.color.dark1));

        // đảm bảo clip bo góc luôn hoạt động
        holder.imgCategory.setClipToOutline(true);

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPos;
            selectedPos = position;
            notifyItemChanged(prev);
            notifyItemChanged(selectedPos);
            if (listener != null) listener.onCategoryClick(category.name);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
