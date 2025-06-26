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
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.dao.ProductDetailDao;
import com.example.bepnhataapp.common.model.ProductDetail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.Locale;

import java.util.List;
import android.content.Intent;
import com.example.bepnhataapp.features.products.ProductDetailActivity;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private Context context;
    private List<Product> list;

    public SuggestionAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Product p = list.get(position);

        String thumb=p.getProductThumb();
        if(thumb!=null&&!thumb.isEmpty()){
            Glide.with(context).load(thumb).placeholder(R.drawable.sample_img).into(holder.imgProduct);
        }else {
            holder.imgProduct.setImageResource(R.drawable.sample_img);
        }

        holder.tvName.setText(p.getProductName());

        // Bind detail data
        ProductDetail detail = new ProductDetailDao(context).getByProductId(p.getProductID());
        if(detail!=null){
            holder.tvKcal.setText((int)detail.getCalo()+" cal");
            holder.tvTime.setText(detail.getCookingTimeMinutes()+" phút");
        }else{
            holder.tvKcal.setText("");
            holder.tvTime.setText("");
        }
        holder.tvNutrition.setText("");
        holder.tvPrice.setText(formatPrice(p.getProductPrice()*(100-p.getSalePercent())/100));

        holder.btnAddCart.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            View sheet = LayoutInflater.from(context).inflate(R.layout.dialog_quick_add, null);
            if(thumb!=null&&!thumb.isEmpty()){
                Glide.with(context).load(thumb).into((ImageView)sheet.findViewById(R.id.imgProduct));
            }
            ((TextView)sheet.findViewById(R.id.tvName)).setText(p.getProductName());
            ((TextView)sheet.findViewById(R.id.tvPrice)).setText(formatPrice(p.getProductPrice()*(100-p.getSalePercent())/100));
            ((TextView)sheet.findViewById(R.id.tvOldPrice)).setText(p.getSalePercent()>0?formatPrice(p.getProductPrice()):"");
            ((TextView)sheet.findViewById(R.id.tvDesc)).setText("Mô tả món ăn ...");
            ((TextView)sheet.findViewById(R.id.tvTotalPrice)).setText(formatPrice(p.getProductPrice()*(100-p.getSalePercent())/100));
            dialog.setContentView(sheet);
            dialog.show();
        });

        // Navigate to product detail when click item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", p.getProductID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvKcal, tvNutrition, tvTime, tvPrice;
        View btnBuy;
        ImageView btnAddCart;
        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvKcal = itemView.findViewById(R.id.tvKcal);
            tvNutrition = itemView.findViewById(R.id.tvNutrition);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
        }
    }

    private String formatPrice(int price){
        NumberFormat nf=NumberFormat.getInstance(new Locale("vi","VN"));
        return nf.format(price)+"đ";
    }
} 