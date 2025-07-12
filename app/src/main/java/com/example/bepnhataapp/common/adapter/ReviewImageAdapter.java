package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ImageViewHolder> {

    private Context context;
    private List<?> images;

    public ReviewImageAdapter(Context context, List<?> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Object obj = images.get(position);
        if(obj instanceof Integer){
            holder.image.setImageResource((Integer)obj);
        } else if(obj instanceof String){
            com.bumptech.glide.Glide.with(context).load((String)obj)
                    .placeholder(R.drawable.food)
                    .error(R.drawable.food)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivReviewImage);
        }
    }
} 
