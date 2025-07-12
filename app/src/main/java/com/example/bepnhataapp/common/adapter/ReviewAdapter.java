package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> list;

    public ReviewAdapter(Context context, List<Review> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = list.get(position);
        if(review.avatarUrl!=null && !review.avatarUrl.isEmpty()){
            Glide.with(context).load(review.avatarUrl).placeholder(R.drawable.profile_placeholder).into(holder.ivAvatar);
        } else if(review.avatarResId!=0){
            holder.ivAvatar.setImageResource(review.avatarResId);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.profile_placeholder);
        }
        holder.tvUserName.setText(review.userName);
        holder.tvTimeAgo.setText(review.timeAgo);
        holder.tvComment.setText(review.comment);
        // For stars, could set ratingBar but using static image placeholder.
        java.util.List<?> imgs = (review.imageResIds!=null && !review.imageResIds.isEmpty()) ? review.imageResIds : review.imageUrls;
        ReviewImageAdapter imgAdapter = new ReviewImageAdapter(context, imgs);
        holder.rvImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvImages.setAdapter(imgAdapter);

        ImageView[] stars = {holder.itemView.findViewById(R.id.ivStar1),
                             holder.itemView.findViewById(R.id.ivStar2),
                             holder.itemView.findViewById(R.id.ivStar3),
                             holder.itemView.findViewById(R.id.ivStar4),
                             holder.itemView.findViewById(R.id.ivStar5)};
        int full = Math.round(review.rating);
        for(int i=0;i<5;i++){
            stars[i].setImageResource(i<full ? R.drawable.ic_star : R.drawable.ic_star_not_filled);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvUserName, tvTimeAgo, tvComment;
        RecyclerView rvImages;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            tvComment = itemView.findViewById(R.id.tvComment);
            rvImages = itemView.findViewById(R.id.rvReviewImages);
        }
    }
} 
