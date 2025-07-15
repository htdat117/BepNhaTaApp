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
            if(tag!=null && !tag.isEmpty()) {
                // Đổi nhãn "Không cholesterol" thành "Ít cholesterol" khi hiển thị
                if(normalize(tag).contains("khongcholesterol")) {
                    tag = "Ít cholesterol";
                }
                holder.tvNutrition.setText(capitalize(tag));
            } else {
                holder.tvNutrition.setText("");
            }
            holder.ivNutrition.setImageResource(getNutritionIconResId(tag));
        }else{
            holder.tvKcal.setText("");
            holder.tvTime.setText("");
            holder.tvNutrition.setText("");
            holder.ivNutrition.setImageResource(getNutritionIconResId(null));
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

        holder.tvRating.setText(String.valueOf((float) product.getAvgRating()));
        holder.tvReviewCount.setText("(" + product.getCommentAmount() + ")");
        boolean isFav = false;
        if (com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(context)) {
            String phoneTmp = com.example.bepnhataapp.common.utils.SessionManager.getPhone(context);
            com.example.bepnhataapp.common.dao.CustomerDao tmpDao = new com.example.bepnhataapp.common.dao.CustomerDao(context);
            com.example.bepnhataapp.common.model.Customer tmpC = tmpDao.findByPhone(phoneTmp);
            if (tmpC != null) {
                com.example.bepnhataapp.common.dao.FavouriteProductDao tmpFavDao = new com.example.bepnhataapp.common.dao.FavouriteProductDao(context);
                isFav = tmpFavDao.isFavourite(product.getProductID(), tmpC.getCustomerID());
            }
        }
        holder.imgFavorite.setImageResource(isFav ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);

        boolean[] favState = new boolean[]{isFav};
        holder.imgFavorite.setOnClickListener(v -> {
            if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(context)) {
                android.widget.Toast.makeText(context, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(context);
            com.example.bepnhataapp.common.dao.CustomerDao cusDao = new com.example.bepnhataapp.common.dao.CustomerDao(context);
            com.example.bepnhataapp.common.model.Customer cus = cusDao.findByPhone(phone);
            if (cus == null) return;

            com.example.bepnhataapp.common.dao.FavouriteProductDao favDao = new com.example.bepnhataapp.common.dao.FavouriteProductDao(context);
            if (favState[0]) {
                favDao.delete(product.getProductID(), cus.getCustomerID());
                android.widget.Toast.makeText(context, "Đã xoá khỏi mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                favDao.insert(new com.example.bepnhataapp.common.model.FavouriteProduct(product.getProductID(), cus.getCustomerID(), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                android.widget.Toast.makeText(context, "Đã thêm vào mục yêu thích", android.widget.Toast.LENGTH_SHORT).show();
            }
            favState[0] = !favState[0];
            holder.imgFavorite.setImageResource(favState[0] ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.bepnhataapp.features.products.ProductDetailActivity.class);
            intent.putExtra("productId", product.getProductID());
            context.startActivity(intent);
        });

        holder.btnAddCart.setOnClickListener(v -> {
            int servingFactor = 1;
            if(servingGroup != null && servingGroup.getCheckedButtonId() == R.id.btnFor4){
                servingFactor = 2;
            }
            com.example.bepnhataapp.common.utils.CartHelper.addProduct(context, product, servingFactor);
            String msg = "Đã thêm vào giỏ hàng thành công";
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút "Mua ngay"
        holder.btnBuy.setOnClickListener(v -> {
            // Lấy serving factor hiện tại
            int servingFactor = 1; // default 2 người
            if (servingGroup != null && servingGroup.getCheckedButtonId() == R.id.btnFor4) {
                servingFactor = 2; // 4 người
            }

            // Tính giá cho serving size hiện tại
            int originalVariantPrice = (servingFactor == 1) ? product.getProductPrice2() : product.getProductPrice4();
            int salePercentVariant = (servingFactor == 1) ? product.getSalePercent2() : product.getSalePercent4();
            int discountedVariantPrice = originalVariantPrice * (100 - salePercentVariant) / 100;

            // Normalise to 2-person pack price because CartItem#getTotal multiplies by servingFactor
            int pricePer2Pack = discountedVariantPrice / servingFactor;
            int oldPricePer2Pack = originalVariantPrice / servingFactor;

            // Tạo CartItem
            com.example.bepnhataapp.common.model.CartItem cartItem = new com.example.bepnhataapp.common.model.CartItem(
                    product.getProductID(),
                    product.getProductName(),
                    pricePer2Pack,
                    oldPricePer2Pack,
                    1, // quantity = 1
                    product.getProductThumb()
            );
            cartItem.setServing(servingFactor == 2 ? "4 người" : "2 người");

            // Tạo danh sách items để chuyển đến checkout
            java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> selectedItems = new java.util.ArrayList<>();
            selectedItems.add(cartItem);

            // Chuyển đến trang thanh toán
            Intent intent = new Intent(context, com.example.bepnhataapp.features.checkout.CheckoutActivity.class);
            intent.putExtra("selected_items", selectedItems);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void refreshViews(ProductViewHolder holder, Product product, ProductDetail detail, int factor){
        if(detail!=null){
            double kcal = factor==1 ? (detail.getCalo2()!=0?detail.getCalo2():detail.getCalo()) : (detail.getCalo4()!=0?detail.getCalo4():detail.getCalo2()*2);
            int time = factor==1 ? (detail.getCookingTimeMinutes2()!=0?detail.getCookingTimeMinutes2():detail.getCookingTimeMinutes()) : (detail.getCookingTimeMinutes4()!=0?detail.getCookingTimeMinutes4():detail.getCookingTimeMinutes2()*2);
            holder.tvKcal.setText((int)kcal+" Kcal");
            holder.tvTime.setText(time+" phút");
        }
        
        // Tính giá cho serving size hiện tại
        int originalPrice = factor==1 ? product.getProductPrice2() : product.getProductPrice4();
        int salePercent = factor==1 ? product.getSalePercent2() : product.getSalePercent4();
        int discountedPrice = originalPrice * (100 - salePercent) / 100;
        
        holder.tvPrice.setText(formatPrice(discountedPrice));

        if(salePercent > 0){
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(formatPrice(originalPrice));
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvOldPrice.setVisibility(View.GONE);
        }
    }

    private String capitalize(String s){
        if(s==null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    /**
     * Return drawable resource id corresponding to nutrition tag keyword.
     * Defaults to generic nutrition icon when tag is unknown or empty.
     */
    private int getNutritionIconResId(String tag){
        if(tag==null || tag.isEmpty()) return R.drawable.ic_nutrition;
        String key = normalize(tag);
        if(key.contains("omega")) return R.drawable.ic_omega3;
        if(key.contains("dam") || key.contains("đam") || key.contains("protein")) return R.drawable.ic_protein_nutrition;
        if(key.contains("vitamin")) return R.drawable.ic_vitamin;
        if(key.contains("cholesterol")) return R.drawable.ic_cholesterol;
        if(key.contains("itbeo") || key.contains("lowfat") || key.contains("chatbeo")) return R.drawable.ic_low_fat;
        if(key.contains("canxi") || key.contains("calcium")) return R.drawable.ic_canxi;
        if(key.contains("chatxo") || key.contains("fiber")) return R.drawable.ic_nutrition; // TODO replace when fiber icon available
        return R.drawable.ic_nutrition;
    }

    // Remove diacritics & make lowercase
    private String normalize(String s){
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        temp = temp.replace('đ','d').replace('Đ','D');
        temp = temp.toLowerCase(java.util.Locale.ROOT).replace("-", "").replace(" ", "");
        return temp;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgFavorite;
        TextView tvProductName, tvKcal, tvNutrition, tvTime, tvFor2, tvFor4, tvOldPrice, tvPrice, tvRating, tvReviewCount;
        ImageView btnAddCart;
        TextView btnBuy;
        ImageView ivNutrition;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            // Apply 16dp rounded corners programmatically to ensure clipping works
            imgProduct.setBackground(androidx.core.content.ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_rounded_16));
            imgProduct.setClipToOutline(true);
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
            btnBuy = itemView.findViewById(R.id.btnBuy);
            ivNutrition = itemView.findViewById(R.id.ivNutritionIcon);
        }
    }
}
