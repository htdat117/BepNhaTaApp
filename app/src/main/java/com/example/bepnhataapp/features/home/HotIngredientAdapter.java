package com.example.bepnhataapp.features.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class HotIngredientAdapter extends RecyclerView.Adapter<HotIngredientAdapter.HotIngredientViewHolder> {

    private List<HotIngredient> hotIngredientList;

    public HotIngredientAdapter(List<HotIngredient> hotIngredientList) {
        this.hotIngredientList = hotIngredientList;
    }

    @NonNull
    @Override
    public HotIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_ingredient, parent, false);
        return new HotIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotIngredientViewHolder holder, int position) {
        HotIngredient hotIngredient = hotIngredientList.get(position);
        holder.itemImage.setImageResource(hotIngredient.getImageResId());
        holder.itemTitle.setText(hotIngredient.getTitle());
        holder.itemPrice.setText(hotIngredient.getPrice());
        // You can set click listeners for the button here if needed
    }

    @Override
    public int getItemCount() {
        return hotIngredientList.size();
    }

    static class HotIngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemPrice;
        Button itemBuyButton;

        public HotIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemBuyButton = itemView.findViewById(R.id.item_buy_button);
        }
    }
} 