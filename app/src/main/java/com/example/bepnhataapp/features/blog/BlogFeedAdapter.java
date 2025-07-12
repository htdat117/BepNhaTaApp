package com.example.bepnhataapp.features.blog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;

import java.util.List;

public class BlogFeedAdapter extends RecyclerView.Adapter<BlogFeedAdapter.BlogPostViewHolder> {

    private List<BlogPostItem> blogList;

    public BlogFeedAdapter(List<BlogPostItem> blogList) {
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_post, parent, false);
        return new BlogPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogPostViewHolder holder, int position) {
        BlogPostItem blog = blogList.get(position);
        holder.imageViewPost.setImageResource(blog.getImageResId());
        holder.textViewTitle.setText(blog.getTitle());
        holder.textViewDescription.setText(blog.getDescription());
        holder.textViewCategory.setText(blog.getCategory());
        holder.textViewDate.setText(blog.getDate());
        holder.textViewViews.setText(String.valueOf(blog.getViews()));
        holder.textViewLikes.setText(String.valueOf(blog.getLikes()));
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public static class BlogPostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPost;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewCategory;
        TextView textViewDate;
        TextView textViewViews;
        TextView textViewLikes;

        public BlogPostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewViews = itemView.findViewById(R.id.textViewViews);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
        }
    }
} 
