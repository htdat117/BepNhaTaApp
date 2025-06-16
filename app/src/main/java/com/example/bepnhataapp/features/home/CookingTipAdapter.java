package com.example.bepnhataapp.features.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class CookingTipAdapter extends RecyclerView.Adapter<CookingTipAdapter.CookingTipViewHolder> {

    private List<CookingTip> cookingTipList;

    public CookingTipAdapter(List<CookingTip> cookingTipList) {
        this.cookingTipList = cookingTipList;
    }

    @NonNull
    @Override
    public CookingTipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest_blog, parent, false);
        return new CookingTipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CookingTipViewHolder holder, int position) {
        CookingTip cookingTip = cookingTipList.get(position);
        holder.tipImage.setImageResource(cookingTip.getImageResId());
        holder.tipTitle.setText(cookingTip.getTitle());
        holder.tipSubtitle.setText(cookingTip.getSubtitle());
        // You can set click listeners for the favorite icon here if needed
    }

    @Override
    public int getItemCount() {
        return cookingTipList.size();
    }

    static class CookingTipViewHolder extends RecyclerView.ViewHolder {
        ImageView tipImage;
        TextView tipTitle;
        TextView tipSubtitle;
        ImageView favoriteTipIcon;

        public CookingTipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipImage = itemView.findViewById(R.id.imgThumb);
            tipTitle = itemView.findViewById(R.id.tvTitle);
            tipSubtitle = itemView.findViewById(R.id.tvCategory);
            favoriteTipIcon = itemView.findViewById(R.id.ivFavorite);
        }
    }
} 