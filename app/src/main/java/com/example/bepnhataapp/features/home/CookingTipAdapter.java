package com.example.bepnhataapp.features.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Blog;
import com.example.bepnhataapp.features.blog.BlogDetailActivity;
import com.example.bepnhataapp.common.utils.SessionManager;

import java.util.List;

public class CookingTipAdapter extends RecyclerView.Adapter<CookingTipAdapter.ViewHolder> {

    private final List<Blog> tips;
    private final Context context;

    public CookingTipAdapter(Context context, List<Blog> tips) {
        this.context = context;
        this.tips = tips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cooking_tip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog tip = tips.get(position);
        holder.title.setText(tip.getTitle());
        holder.category.setText(tip.getCategory());

        Glide.with(context)
                .load(tip.getImageUrl())
                .placeholder(R.drawable.placeholder_banner_background)
                .error(R.drawable.placeholder_banner_background)
                .into(holder.image);

        // Nếu chưa đăng nhập thì luôn hiển thị tim rỗng
        if (!SessionManager.isLoggedIn(context)) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_unchecked);
        } else {
            holder.favoriteIcon.setImageResource(tip.isFavorite() ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
        }

        holder.favoriteIcon.setOnClickListener(v -> {
            if (!SessionManager.isLoggedIn(context)) {
                android.widget.Toast.makeText(context, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            tip.setFavorite(!tip.isFavorite());
            notifyItemChanged(position);
            if (tip.isFavorite()) {
                android.widget.Toast.makeText(context, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                android.widget.Toast.makeText(context, "Đã xoá khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
            }
            // Here you would also update the database
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BlogDetailActivity.class);
            intent.putExtra(BlogDetailActivity.EXTRA_BLOG, tip);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tips != null ? tips.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView category;
        ImageView favoriteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tip_image);
            title = itemView.findViewById(R.id.tip_title);
            category = itemView.findViewById(R.id.tip_category);
            favoriteIcon = itemView.findViewById(R.id.tip_favorite_icon);
        }
    }
} 
