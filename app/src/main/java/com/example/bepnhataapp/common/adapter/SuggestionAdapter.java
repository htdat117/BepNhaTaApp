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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

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
        holder.imgProduct.setImageResource(p.imageResId);
        holder.tvName.setText(p.name);
        holder.tvKcal.setText(p.kcal);
        holder.tvNutrition.setText(p.nutrition);
        holder.tvTime.setText(p.time);
        holder.tvPrice.setText(p.price);
        holder.btnAddCart.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            View sheet = LayoutInflater.from(context).inflate(R.layout.dialog_quick_add, null);
            ((ImageView)sheet.findViewById(R.id.imgProduct)).setImageResource(p.imageResId);
            ((TextView)sheet.findViewById(R.id.tvName)).setText(p.name);
            ((TextView)sheet.findViewById(R.id.tvPrice)).setText(p.price);
            ((TextView)sheet.findViewById(R.id.tvOldPrice)).setText(p.oldPrice);
            ((TextView)sheet.findViewById(R.id.tvDesc)).setText("Mô tả món ăn ...");
            ((TextView)sheet.findViewById(R.id.tvTotalPrice)).setText(p.price);
            dialog.setContentView(sheet);
            dialog.show();
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
} 