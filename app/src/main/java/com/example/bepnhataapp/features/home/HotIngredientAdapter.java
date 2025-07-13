package com.example.bepnhataapp.features.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.bumptech.glide.Glide;
import com.example.bepnhataapp.common.model.Product;

import java.util.List;

public class HotIngredientAdapter extends RecyclerView.Adapter<HotIngredientAdapter.HotIngredientViewHolder> {

    private List<Product> productList;

    public HotIngredientAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public HotIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_ingredient, parent, false);
        return new HotIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotIngredientViewHolder holder, int position) {
        Product product = productList.get(position);
        String img = product.getProductThumb();
        Object source;
        if (img == null || img.isEmpty()) {
            source = R.drawable.placeholder_banner_background;
        } else if (img.startsWith("http")) {
            source = img;
        } else {
            int resId = holder.itemView.getContext().getResources()
                    .getIdentifier(img, "drawable", holder.itemView.getContext().getPackageName());
            source = resId != 0 ? resId : R.drawable.placeholder_banner_background;
        }

        Glide.with(holder.itemView.getContext())
                .load(source)
                .placeholder(R.drawable.placeholder_banner_background)
                .error(R.drawable.placeholder_banner_background)
                .into(holder.itemImage);
        holder.itemTitle.setText(product.getProductName());
        holder.itemPrice.setText(String.format("%,dđ", product.getProductPrice()).replace(',', '.'));

        // "Mua ngay" -> build temporary CartItem list & open CheckoutActivity directly
        holder.itemBuyButton.setOnClickListener(v -> {
            Context ctx = v.getContext();
            int salePrice = product.getProductPrice() * (100 - product.getSalePercent()) / 100;
            com.example.bepnhataapp.common.model.CartItem ci = new com.example.bepnhataapp.common.model.CartItem(
                    product.getProductID(),
                    product.getProductName(),
                    salePrice,
                    product.getProductPrice(),
                    1,
                    product.getProductThumb()
            );
            java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> selected = new java.util.ArrayList<>();
            selected.add(ci);

            android.content.Intent intent = new android.content.Intent(ctx, com.example.bepnhataapp.features.checkout.CheckoutActivity.class);
            intent.putExtra("selected_items", selected);
            ctx.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, com.example.bepnhataapp.features.products.ProductDetailActivity.class);
            intent.putExtra("productId", product.getProductID());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
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
