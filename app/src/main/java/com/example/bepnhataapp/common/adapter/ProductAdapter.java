package com.example.bepnhataapp.common.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.imgProduct.setImageResource(product.imageResId);
        holder.tvProductName.setText(product.name);
        holder.tvKcal.setText(product.kcal);
        holder.tvNutrition.setText(product.nutrition);
        holder.tvTime.setText(product.time);
        holder.tvFor2.setText(product.for2);
        holder.tvFor4.setText(product.for4);
        holder.tvOldPrice.setText(product.oldPrice);
        holder.tvPrice.setText(product.price);
        holder.tvRating.setText(String.valueOf(product.rating));
        holder.tvReviewCount.setText("(" + product.reviewCount + ")");
        holder.imgFavorite.setImageResource(product.isFavorite ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgFavorite;
        TextView tvProductName, tvKcal, tvNutrition, tvTime, tvFor2, tvFor4, tvOldPrice, tvPrice, tvRating, tvReviewCount, btnBuy;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvKcal = itemView.findViewById(R.id.tvKcal);
            tvNutrition = itemView.findViewById(R.id.tvNutrition);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvFor2 = itemView.findViewById(R.id.btnFor2);
            tvFor4 = itemView.findViewById(R.id.btnFor4);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }
}