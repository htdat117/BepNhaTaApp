package com.example.bepnhataapp.common.adapter;

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

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Blog> blogList;
    private Context context;

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
        context = parent.getContext(); // Lấy context từ parent
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_blog_post, parent, false);
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

            Glide.with(context)
                    .load(blog.getImageUrl())
                    .placeholder(R.drawable.placeholder_banner_background)
                    .into(vh.imageViewPost);

            vh.textViewCategory.setText(blog.getCategory());
            vh.textViewTitle.setText(blog.getTitle());
            vh.textViewDescription.setText(blog.getDescription());
            vh.textViewDate.setText(blog.getDate());
            vh.textViewViews.setText(String.valueOf(blog.getViews()));
            vh.textViewLikes.setText(String.valueOf(blog.getLikes()));
            vh.imageViewLike.setImageResource(blog.isFavorite() ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
            vh.imageViewLike.setOnClickListener(v -> {
                // Only allow for logged-in user
                if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(context)) {
                    android.widget.Toast.makeText(context, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(context);
                if (phone == null) return;
                com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(context);
                com.example.bepnhataapp.common.model.Customer c = cDao.findByPhone(phone);
                if (c == null) return;

                com.example.bepnhataapp.common.dao.FavouriteBlogDao favDao = new com.example.bepnhataapp.common.dao.FavouriteBlogDao(context);
                boolean isFav = favDao.isFavourite(blog.getBlogID(), c.getCustomerID());
                if (isFav) {
                    favDao.delete(blog.getBlogID(), c.getCustomerID());
                    android.widget.Toast.makeText(context, "Đã xoá khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    favDao.insert(new com.example.bepnhataapp.common.model.FavouriteBlog(blog.getBlogID(), c.getCustomerID(), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                    android.widget.Toast.makeText(context, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                }

                blog.setFavorite(!isFav);
                notifyItemChanged(position);
            });
            vh.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BlogDetailActivity.class);
                intent.putExtra(BlogDetailActivity.EXTRA_BLOG, blog);
                context.startActivity(intent);
            });
        } else {
            ItemViewHolder vh = (ItemViewHolder) holder;
            Glide.with(context)
                    .load(blog.getImageUrl())
                    .placeholder(R.drawable.placeholder_banner_background)
                    .into(vh.imgBlog);
            vh.tvTitle.setText(blog.getTitle());
            vh.tvCategory.setText(blog.getCategory());
            vh.imgFavorite.setImageResource(blog.isFavorite() ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
            vh.imgFavorite.setOnClickListener(v -> {
                // Only allow for logged-in user
                if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(context)) {
                    android.widget.Toast.makeText(context, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(context);
                if (phone == null) return;
                com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(context);
                com.example.bepnhataapp.common.model.Customer c = cDao.findByPhone(phone);
                if (c == null) return;

                com.example.bepnhataapp.common.dao.FavouriteBlogDao favDao = new com.example.bepnhataapp.common.dao.FavouriteBlogDao(context);
                boolean isFav = favDao.isFavourite(blog.getBlogID(), c.getCustomerID());
                if (isFav) {
                    favDao.delete(blog.getBlogID(), c.getCustomerID());
                    android.widget.Toast.makeText(context, "Đã xoá khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    favDao.insert(new com.example.bepnhataapp.common.model.FavouriteBlog(blog.getBlogID(), c.getCustomerID(), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                    android.widget.Toast.makeText(context, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                }

                blog.setFavorite(!isFav);
                notifyItemChanged(position);
            });
            vh.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BlogDetailActivity.class);
                intent.putExtra(BlogDetailActivity.EXTRA_BLOG, blog);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPost, imageViewLike;
        TextView textViewCategory, textViewTitle, textViewDescription, textViewDate, textViewViews, textViewLikes;
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            imageViewLike = itemView.findViewById(R.id.imageViewLike);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewViews = itemView.findViewById(R.id.textViewViews);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
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
