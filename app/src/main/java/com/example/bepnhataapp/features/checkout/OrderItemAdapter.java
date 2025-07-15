package com.example.bepnhataapp.features.checkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.CartItem;
import com.bumptech.glide.Glide;
import android.graphics.Paint;

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
        holder.tvProductName.setText(item.getTitle());
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));
        int factor = item.getServing().startsWith("4")?2:1;
        holder.tvPrice.setText(nf.format(item.getPrice()*factor)+"đ");
        holder.tvOldPrice.setText(nf.format(item.getOldPrice()*factor)+"đ");
        holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvVariant.setText("Phân loại: "+item.getServing());
        holder.tvQuantity.setText("x"+item.getQuantity());
        Glide.with(holder.imgProduct.getContext())
                .load(item.getThumb())
                .placeholder(R.drawable.sample_img)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvVariant, tvPrice, tvOldPrice, tvQuantity;
        ImageView imgProduct;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvVariant = itemView.findViewById(R.id.tvVariant);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
} 
