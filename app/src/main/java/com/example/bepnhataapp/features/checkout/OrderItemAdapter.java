package com.example.bepnhataapp.features.checkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.CartItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {

    private final List<CartItem> items;

    public OrderItemAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_product, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvPrice.setText(item.getPrice());
        holder.tvVariant.setText("Phân loại: " + item.getServing());
        holder.tvQuantity.setText("x1");
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvVariant, tvPrice, tvQuantity;
        ImageView imgProduct;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvVariant = itemView.findViewById(R.id.tvVariant);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
} 