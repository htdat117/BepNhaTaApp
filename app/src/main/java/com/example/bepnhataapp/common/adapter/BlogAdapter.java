package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.Blog;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Blog> blogList;

    public BlogAdapter(List<Blog> blogList) {
        this.blogList = blogList;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_blog_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_blog_small, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.imgBlogHeader.setImageResource(blog.getImageResId());
            vh.tvTitleHeader.setText(blog.getTitle());
            vh.tvDescHeader.setText(blog.getDescription());
            vh.tvCategoryHeader.setText(blog.getCategory());
            vh.imgFavoriteHeader.setImageResource(blog.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
            vh.imgFavoriteHeader.setOnClickListener(v -> {
                blog.setFavorite(!blog.isFavorite());
                notifyItemChanged(position);
            });
        } else {
            ItemViewHolder vh = (ItemViewHolder) holder;
            vh.imgBlog.setImageResource(blog.getImageResId());
            vh.tvTitle.setText(blog.getTitle());
            vh.tvCategory.setText(blog.getCategory());
            vh.imgFavorite.setImageResource(blog.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
            vh.imgFavorite.setOnClickListener(v -> {
                blog.setFavorite(!blog.isFavorite());
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBlogHeader, imgFavoriteHeader;
        TextView tvTitleHeader, tvDescHeader, tvCategoryHeader;
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBlogHeader = itemView.findViewById(R.id.imgBlogHeader);
            imgFavoriteHeader = itemView.findViewById(R.id.imgFavoriteHeader);
            tvTitleHeader = itemView.findViewById(R.id.tvTitleHeader);
            tvDescHeader = itemView.findViewById(R.id.tvDescHeader);
            tvCategoryHeader = itemView.findViewById(R.id.tvCategoryHeader);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBlog, imgFavorite;
        TextView tvTitle, tvCategory;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBlog = itemView.findViewById(R.id.imgBlog);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
} 