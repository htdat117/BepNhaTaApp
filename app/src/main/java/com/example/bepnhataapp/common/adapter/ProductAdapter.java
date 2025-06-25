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

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.features.products.ProductDetailActivity;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.Locale;
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

        // Load image thumb via Glide
        String thumb = product.getProductThumb();
        if (thumb != null && !thumb.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(thumb)
                    .placeholder(R.drawable.sample_img)
                    .error(R.drawable.sample_img)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.sample_img);
        }

        holder.tvProductName.setText(product.getProductName());
        holder.tvKcal.setText("");
        holder.tvNutrition.setText("");
        holder.tvTime.setText("");
        holder.tvFor2.setText("2 người");
        holder.tvFor4.setText("4 người");

        int price = product.getProductPrice() * (100 - product.getSalePercent()) / 100;
        String priceStr = formatPrice(price);
        holder.tvPrice.setText(priceStr);
        if (product.getSalePercent() > 0) {
            holder.tvOldPrice.setText(formatPrice(product.getProductPrice()));
            holder.tvOldPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
        }

        holder.tvRating.setText(String.valueOf((float) product.getAvgRating()));
        holder.tvReviewCount.setText("(" + product.getCommentAmount() + ")");
        holder.imgFavorite.setImageResource(R.drawable.ic_favorite_unchecked);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.bepnhataapp.features.products.ProductDetailActivity.class);
            intent.putExtra("productId", product.getProductID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private String formatPrice(int price){
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi","VN"));
        return nf.format(price)+"đ";
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