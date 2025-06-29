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
import com.example.bepnhataapp.common.dao.ProductDetailDao;
import com.example.bepnhataapp.common.model.ProductDetail;
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
        ProductDetail detail = new ProductDetailDao(context).getByProductId(product.getProductID());
        if(detail!=null){
            holder.tvKcal.setText((int)detail.getCalo()+" Kcal");
            holder.tvTime.setText(detail.getCookingTimeMinutes()+" phút");
            String tag = detail.getNutritionTag();
            if(tag!=null && !tag.isEmpty()) holder.tvNutrition.setText(capitalize(tag));
            else holder.tvNutrition.setText("");
        }else{
            holder.tvKcal.setText("");
            holder.tvTime.setText("");
            holder.tvNutrition.setText("");
        }
        holder.tvFor2.setText("2 người");
        holder.tvFor4.setText("4 người");

        // Serving toggle listener
        com.google.android.material.button.MaterialButtonToggleGroup servingGroup = holder.itemView.findViewById(R.id.toggleGroupServing);
        if(servingGroup!=null){
            servingGroup.addOnButtonCheckedListener((g, checkedId, isChecked) -> {
                if(!isChecked) return;
                int factor = checkedId == R.id.btnFor2 ? 1 : 2;
                refreshViews(holder, product, detail, factor);
            });
            // default factor 1
            refreshViews(holder, product, detail, 1);
        }

        int price2 = product.getProductPrice2() * (100 - product.getSalePercent2()) / 100;
        String priceStr = formatPrice(price2);
        holder.tvPrice.setText(priceStr);
        if (product.getSalePercent2() > 0) {
            holder.tvOldPrice.setText(formatPrice(product.getProductPrice2()));
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
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

        holder.btnAddCart.setOnClickListener(v -> {
            com.example.bepnhataapp.common.utils.CartHelper.addProduct(context, product);
            android.widget.Toast.makeText(context, "Đã thêm giỏ hàng thành công", android.widget.Toast.LENGTH_SHORT).show();
            // Muốn mở giỏ ngay:
            // context.startActivity(new Intent(context, com.example.bepnhataapp.features.cart.CartActivity.class));
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

    private void refreshViews(ProductViewHolder holder, Product product, ProductDetail detail, int factor){
        if(detail!=null){
            double kcal = factor==1 ? (detail.getCalo2()!=0?detail.getCalo2():detail.getCalo()) : (detail.getCalo4()!=0?detail.getCalo4():detail.getCalo2()*2);
            int time = factor==1 ? (detail.getCookingTimeMinutes2()!=0?detail.getCookingTimeMinutes2():detail.getCookingTimeMinutes()) : (detail.getCookingTimeMinutes4()!=0?detail.getCookingTimeMinutes4():detail.getCookingTimeMinutes2()*2);
            holder.tvKcal.setText((int)kcal+" Kcal");
            holder.tvTime.setText(time+" phút");
        }
        int basePrice = factor==1 ? product.getProductPrice2()*(100-product.getSalePercent2())/100
                                  : product.getProductPrice4()*(100-product.getSalePercent4())/100;
        holder.tvPrice.setText(formatPrice(basePrice));

        if((factor==1 && product.getSalePercent2()>0) || (factor==2 && product.getSalePercent4()>0)){
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            int old = factor==1? product.getProductPrice2() : product.getProductPrice4();
            holder.tvOldPrice.setText(formatPrice(old));
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
        }
    }

    private String capitalize(String s){
        if(s==null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgFavorite;
        TextView tvProductName, tvKcal, tvNutrition, tvTime, tvFor2, tvFor4, tvOldPrice, tvPrice, tvRating, tvReviewCount;
        ImageView btnAddCart;
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
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
        }
    }
}